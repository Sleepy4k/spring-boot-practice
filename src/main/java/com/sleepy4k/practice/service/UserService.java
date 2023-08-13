package com.sleepy4k.practice.service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.sleepy4k.practice.api.model.User;

@Service
public class UserService {
  private List<User> usersList;

  public UserService() {
    usersList = new ArrayList<User>();

    User user1 = new User(1, "John", 20, "john@example.com");
    User user2 = new User(2, "Jane", 21, "jane@example.com");
    User user3 = new User(3, "Joe", 22, "joe@example.com");
    User user4 = new User(4, "Jill", 23, "jill@example.com");
    User user5 = new User(5, "Jack", 24, "jack@example.com");

    usersList.addAll(Arrays.asList(user1, user2, user3, user4, user5));
  }

  public List<User> getUser() {
    return usersList;
  }

  public User addUser(User body) {
    int id = usersList.size() + 1;
    String name = body.getName();
    int age = body.getAge();
    String email = body.getEmail();

    User user = new User(id, name, age, email);

    usersList.add(user);

    return user;
  }

  public Optional<User> findUser(int id) {
    Optional<User> optional = Optional.empty();

    for (User user : usersList) {
      if (user.getId() == id) {
        optional = Optional.of(user);
        break;
      }
    }

    return optional;
  }

  public Optional<User> editUser(int id, User body) {
    Optional<User> optional = Optional.empty();

    for (User user : usersList) {
      if (user.getId() == id) {
        user.setName(body.getName());
        user.setAge(body.getAge());
        user.setEmail(body.getEmail());

        optional = Optional.of(user);
        break;
      }
    }

    return optional;
  }

  public Optional<User> deleteUser(int id) {
    Optional<User> optional = Optional.empty();

    for (User user : usersList) {
      if (user.getId() == id) {
        usersList.remove(user);

        optional = Optional.of(user);
        break;
      }
    }

    return optional;
  }
}
