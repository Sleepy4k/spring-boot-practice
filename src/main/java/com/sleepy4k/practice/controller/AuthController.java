package com.sleepy4k.practice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.service.AuthService;
import com.sleepy4k.practice.model.TokenResponse;
import com.sleepy4k.practice.model.LoginUserRequest;
import com.sleepy4k.practice.model.RegisterUserRequest;

@RestController
public class AuthController {
  @Autowired
  private AuthService authService;

  @PostMapping(path = "/api/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    authService.register(request);

    return WebResponse.<String>builder().data("OK").build();
  }

  @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
    TokenResponse tokenResponse = authService.login(request);

    return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
  }

  @DeleteMapping(path = "/api/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> logout(User user) {
    authService.logout(user);

    return WebResponse.<String>builder().data("OK").build();
  }
}
