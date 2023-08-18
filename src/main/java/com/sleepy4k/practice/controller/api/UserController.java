package com.sleepy4k.practice.controller.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.UserResponse;
import com.sleepy4k.practice.service.api.UserService;
import com.sleepy4k.practice.request.UpdateUserRequest;

@RestController
@Component("APIUserController")
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> me(User user) {
    UserResponse userResponse = userService.me(user);

    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }

  @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
    UserResponse userResponse = userService.update(user, request);

    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }
}
