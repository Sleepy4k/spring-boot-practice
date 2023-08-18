package com.sleepy4k.practice.controller.web;

import org.springframework.ui.Model;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@Component("WEBAuthController")
public class AuthController {
  @Autowired
  private Environment env;

  @GetMapping("/login")
  public String index(Model model) {
    model.addAttribute("appName", env.getProperty("spring.application.name"));

    return "pages/login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    model.addAttribute("appName", env.getProperty("spring.application.name"));

    return "pages/register";
  }
}
