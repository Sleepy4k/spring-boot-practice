package com.sleepy4k.practice.controller.web;

import org.springframework.ui.Model;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Component("WEBWelcomeController")
public class WelcomeController {
  @Autowired
  private Environment env;

  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("appName", env.getProperty("spring.application.name"));
    model.addAttribute("appVersion", env.getProperty("spring.application.version"));
    model.addAttribute("appDescription", env.getProperty("spring.application.description"));

    return "pages/landing";
  }
}
