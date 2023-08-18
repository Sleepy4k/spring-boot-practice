package com.sleepy4k.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.TokenResponse;
import com.sleepy4k.practice.repository.UserRepository;
import com.sleepy4k.practice.request.LoginUserRequest;
import com.sleepy4k.practice.request.RegisterUserRequest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void testRegisterSuccess() throws Exception {
    RegisterUserRequest request = new RegisterUserRequest();
    request.setUsername("user");
    request.setPassword("password");
    request.setName("User");

    mockMvc.perform(
      post("/api/auth/register")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertEquals("OK", response.getData());
    });
  }

  @Test
  void testRegisterBadRequest() throws Exception {
    RegisterUserRequest request = new RegisterUserRequest();
    request.setUsername("");
    request.setPassword("");
    request.setName("");

    mockMvc.perform(
      post("/api/auth/register")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testRegisterDuplicate() throws Exception {
    User user = new User();
    user.setUsername("user");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setName("User");

    userRepository.save(user);

    RegisterUserRequest request = new RegisterUserRequest();
    request.setUsername("user");
    request.setPassword("password");
    request.setName("User");

    mockMvc.perform(
      post("/api/auth/register")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testLoginNotFound() throws Exception {
    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("password");

    mockMvc.perform(
      post("/api/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testLoginInvalidPassword() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password123", BCrypt.gensalt()));

    userRepository.save(user);

    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("password");

    mockMvc.perform(
      post("/api/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testLoginSuccess() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));

    userRepository.save(user);

    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("password");

    mockMvc.perform(
      post("/api/auth/login")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {});

      assertNull(response.getErrors());
      assertNotNull(response.getData().getToken());
      assertNotNull(response.getData().getExpiredAt());

      User userDB = userRepository.findById("test").orElse(null);

      assertNotNull(userDB);
      assertEquals(userDB.getToken(), response.getData().getToken());
      assertEquals(userDB.getTokenExpiredAt(), response.getData().getExpiredAt());
    });
  }

  @Test
  void testLogoutUnautorized() throws Exception {
    mockMvc.perform(
      delete("/api/auth/logout")
        .accept(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testLogoutSuccess() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setToken("Token123");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);

    userRepository.save(user);

    mockMvc.perform(
      delete("/api/auth/logout")
        .accept(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "Token123")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNull(response.getErrors());
      assertEquals("OK", response.getData());

      User userDB = userRepository.findById("test").orElse(null);

      assertNotNull(userDB);
      assertEquals("test", userDB.getUsername());
      assertEquals("Test", userDB.getName());
      assertTrue(BCrypt.checkpw("password", userDB.getPassword()));
      assertNull(userDB.getToken());
      assertNull(userDB.getTokenExpiredAt());
    });
  }
}
