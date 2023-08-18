package com.sleepy4k.practice.controller.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.WelcomeResponse;

@RestController
@Component("APIWelcomeController")
public class WelcomeController {
  @Autowired
  private Environment env;

  @RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<WelcomeResponse> api() {
    return WebResponse.<WelcomeResponse>builder()
      .data(WelcomeResponse.builder()
        .name(env.getProperty("spring.application.name"))
        .version(env.getProperty("spring.application.version"))
        .description(env.getProperty("spring.application.description"))
        .build())
      .build();
  }
}
