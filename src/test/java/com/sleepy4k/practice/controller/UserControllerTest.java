package com.sleepy4k.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.UserResponse;
import com.sleepy4k.practice.repository.UserRepository;
import com.sleepy4k.practice.request.UpdateUserRequest;

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
public class UserControllerTest {
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
  void testGetUserUnautorized() throws Exception {
    mockMvc.perform(
      get("/api/users/me")
        .accept(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "some token")
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetUserUnautorizedTokenMissing() throws Exception {
    mockMvc.perform(
      get("/api/users/me")
        .accept(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetUserUnautorizedExpired() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setToken("Token123");
    user.setTokenExpiredAt(System.currentTimeMillis() - 1000000);

    userRepository.save(user);

    mockMvc.perform(
      get("/api/users/me")
        .accept(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetUserSuccess() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setToken("Token123");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);

    userRepository.save(user);

    mockMvc.perform(
      get("/api/users/me")
        .accept(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "Token123")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {});

      assertNull(response.getErrors());
      assertEquals("test", response.getData().getUsername());
      assertEquals("Test", response.getData().getName());
    });
  }

  @Test
  void testUpdateUserUnautorized() throws Exception {
    UpdateUserRequest request = new UpdateUserRequest();

    mockMvc.perform(
      patch("/api/users/me")
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
  void testUpdateUserSuccess() throws Exception {
    User user = new User();
    user.setName("Test");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setToken("Token123");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);

    userRepository.save(user);

    UpdateUserRequest request = new UpdateUserRequest();
    request.setName("Test123");
    request.setPassword("password123");

    mockMvc.perform(
      patch("/api/users/me")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", "Token123")
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {});

      assertNull(response.getErrors());
      assertEquals("test", response.getData().getUsername());
      assertEquals("Test123", response.getData().getName());

      User userDB = userRepository.findById("test").orElse(null);

      assertNotNull(userDB);
      assertEquals("test", userDB.getUsername());
      assertEquals("Test123", userDB.getName());
      assertTrue(BCrypt.checkpw("password123", userDB.getPassword()));
    });
  }
}
