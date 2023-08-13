package com.sleepy4k.practice.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
  @RequestMapping("/")
  public String index() {
    return "Welcome to the practice API using Spring Boot!";
  }
}
