package com.sleepy4k.practice.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.service.ValidationService;
import com.sleepy4k.practice.model.TokenResponse;
import com.sleepy4k.practice.repository.UserRepository;
import com.sleepy4k.practice.request.LoginUserRequest;
import com.sleepy4k.practice.request.RegisterUserRequest;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationService validationService;

  @Transactional
  public void register(RegisterUserRequest request) {
    validationService.validate(request);

    if (userRepository.existsById(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    user.setName(request.getName());

    userRepository.save(user);
  }

  @Transactional
  public TokenResponse login(LoginUserRequest request) {
    validationService.validate(request);

    User user = userRepository.findById(request.getUsername())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

    if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 16 * 24 * 30));

      userRepository.save(user);

      return TokenResponse.builder()
        .token(user.getToken())
        .expiredAt(user.getTokenExpiredAt())
        .build();
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
  }

  @Transactional
  public void logout(User user) {
    user.setToken(null);
    user.setTokenExpiredAt(null);

    userRepository.save(user);
  }
}
