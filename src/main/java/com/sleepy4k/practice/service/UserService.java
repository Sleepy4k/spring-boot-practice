package com.sleepy4k.practice.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.model.UserResponse;
import com.sleepy4k.practice.model.UpdateUserRequest;
import com.sleepy4k.practice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationService validationService;

  public UserResponse me(User user) {
    return UserResponse.builder()
      .username(user.getUsername())
      .name(user.getName())
      .build();
  }

  @Transactional
  public UserResponse update(User user, UpdateUserRequest request) {
    validationService.validate(request);

    if (Objects.nonNull(request.getName())) {
      user.setName(request.getName());
    }

    if (Objects.nonNull(request.getPassword())) {
      user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    }

    userRepository.save(user);

    return UserResponse.builder()
      .username(user.getUsername())
      .name(user.getName())
      .build();
  }
}
