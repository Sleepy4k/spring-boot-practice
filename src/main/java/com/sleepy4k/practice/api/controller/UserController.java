package com.sleepy4k.practice.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.api.model.User;
import com.sleepy4k.practice.service.UserService;

@RestController
public class UserController {
  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value="/user")
  public List<User> getUser() {
    return userService.getUser();
  }

  @PostMapping(value="/user")
  public User addUser(@RequestBody User body) {
    return userService.addUser(body);
  }

  @GetMapping(value="/user/{id}")
  public User findUser(@PathVariable("id") int id) {
    Optional<User> user = userService.findUser(id);

    if (user.isPresent()) {
      return (User) user.get();
    } else {
      return new User();
    }
  }

  @PutMapping(value="user/{id}")
  public User editUser(@PathVariable("id") int id, @RequestBody User body) {
    Optional<User> user = userService.editUser(id, body);

    if (user.isPresent()) {
      return (User) user.get();
    } else {
      return new User();
    }
  }

  @DeleteMapping(value="user/{id}")
  public User deleteUser(@PathVariable("id") int id) {
    Optional<User> user = userService.deleteUser(id);

    if (user.isPresent()) {
      return (User) user.get();
    } else {
      return new User();
    }
  }
}
