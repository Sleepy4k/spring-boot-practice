package com.sleepy4k.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.entity.Contact;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.ContactResponse;
import com.sleepy4k.practice.repository.UserRepository;
import com.sleepy4k.practice.request.CreateContactRequest;
import com.sleepy4k.practice.request.UpdateContactRequest;
import com.sleepy4k.practice.repository.ContactRepository;

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

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    contactRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("user");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setName("User");
    user.setToken("token");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);

    userRepository.save(user);
  }

  @AfterEach
  void tearDown() {
    contactRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void testCreateContactBadRequest() throws Exception {
    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("");
    request.setEmail("example");

    mockMvc.perform(
      post("/api/contacts")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testCreateContactSuccess() throws Exception {
    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("Sleepy");
    request.setLastName("4k");
    request.setEmail("sleepy4k@example.com");
    request.setPhone("08123456789");

    mockMvc.perform(
      post("/api/contacts")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {});

      assertNull(response.getErrors());
      assertEquals("Sleepy", response.getData().getFirstName());
      assertEquals("4k", response.getData().getLastName());
      assertEquals("sleepy4k@example.com", response.getData().getEmail());
      assertEquals("08123456789", response.getData().getPhone());

      assertTrue(contactRepository.existsById(response.getData().getId()));
    });
  }

  @Test
  void testGetContactNotFound() throws Exception {
    mockMvc.perform(
      get("/api/contacts/69")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetContactSuccess() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setFirstName("Sleepy");
    contact.setLastName("4k");
    contact.setEmail("sleepy4k@example.com");
    contact.setPhone("08123456789");
    contact.setUser(user);

    contactRepository.save(contact);

    mockMvc.perform(
      get("/api/contacts/" + contact.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {});

      assertNull(response.getErrors());
      assertEquals(contact.getId(), response.getData().getId());
      assertEquals(contact.getFirstName(), response.getData().getFirstName());
      assertEquals(contact.getLastName(), response.getData().getLastName());
      assertEquals(contact.getEmail(), response.getData().getEmail());
      assertEquals(contact.getPhone(), response.getData().getPhone());
    });
  }

  @Test
  void testUpdateContactBadRequest() throws Exception {
    UpdateContactRequest request = new UpdateContactRequest();
    request.setFirstName("");
    request.setEmail("example");

    mockMvc.perform(
      put("/api/contacts/69")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testUpdateContactSuccess() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setFirstName("Sleepy");
    contact.setLastName("4k");
    contact.setEmail("sleepy4k@example.com");
    contact.setPhone("08123456789");
    contact.setUser(user);

    contactRepository.save(contact);

    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("Stewie");
    request.setLastName("2k");
    request.setEmail("Stewie2k@example.com");
    request.setPhone("08987654321");

    mockMvc.perform(
      put("/api/contacts/" + contact.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {});

      assertNull(response.getErrors());
      assertEquals(request.getFirstName(), response.getData().getFirstName());
      assertEquals(request.getLastName(), response.getData().getLastName());
      assertEquals(request.getEmail(), response.getData().getEmail());
      assertEquals(request.getPhone(), response.getData().getPhone());

      assertTrue(contactRepository.existsById(response.getData().getId()));
    });
  }

  @Test
  void testDeleteContactNotFound() throws Exception {
    mockMvc.perform(
      delete("/api/contacts/69")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testDeleteContactSuccess() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setFirstName("Sleepy");
    contact.setLastName("4k");
    contact.setEmail("sleepy4k@example.com");
    contact.setPhone("08123456789");
    contact.setUser(user);

    contactRepository.save(contact);

    mockMvc.perform(
      delete("/api/contacts/" + contact.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNull(response.getErrors());
      assertEquals("OK", response.getData());
    });
  }

  @Test
  void testSearchContactNotFound() throws Exception {
    mockMvc.perform(
      get("/api/contacts")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(0, response.getData().size());
      assertEquals(0, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }

  @Test
  void testSearchContactByName() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    for (int i = 0; i < 100; i++) {
      Contact contact = new Contact();
      contact.setId(UUID.randomUUID().toString());
      contact.setFirstName("Sleepy" + i);
      contact.setLastName("kz");
      contact.setEmail("sleepy" + i + "kz@example.com");
      contact.setPhone("08123456789" + i);
      contact.setUser(user);

      contactRepository.save(contact);
    }

    mockMvc.perform(
      get("/api/contacts")
        .queryParam("name", "Sleepy")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(10, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });

    mockMvc.perform(
      get("/api/contacts")
        .queryParam("name", "kz")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(10, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }

  @Test
  void testSearchContactByEmail() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    for (int i = 0; i < 100; i++) {
      Contact contact = new Contact();
      contact.setId(UUID.randomUUID().toString());
      contact.setFirstName("Sleepy" + i);
      contact.setLastName("kz");
      contact.setEmail("sleepy" + i + "kz@example.com");
      contact.setPhone("08123456789" + i);
      contact.setUser(user);

      contactRepository.save(contact);
    }

    mockMvc.perform(
      get("/api/contacts")
        .queryParam("email", "example.com")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(10, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }

  @Test
  void testSearchContactByPhone() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    for (int i = 0; i < 100; i++) {
      Contact contact = new Contact();
      contact.setId(UUID.randomUUID().toString());
      contact.setFirstName("Sleepy" + i);
      contact.setLastName("kz");
      contact.setEmail("sleepy" + i + "kz@example.com");
      contact.setPhone("08123456789" + i);
      contact.setUser(user);

      contactRepository.save(contact);
    }

    mockMvc.perform(
      get("/api/contacts")
        .queryParam("phone", "08123456789")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(10, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }

  @Test
  void testSearchContactByPaging() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    for (int i = 0; i < 100; i++) {
      Contact contact = new Contact();
      contact.setId(UUID.randomUUID().toString());
      contact.setFirstName("Sleepy" + i);
      contact.setLastName("kz");
      contact.setEmail("sleepy" + i + "kz@example.com");
      contact.setPhone("08123456789" + i);
      contact.setUser(user);

      contactRepository.save(contact);
    }

    mockMvc.perform(
      get("/api/contacts")
        .queryParam("email", "example.com")
        .queryParam("page", "1000")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", "token")
    ).andExpect(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(0, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(1000, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }
}
