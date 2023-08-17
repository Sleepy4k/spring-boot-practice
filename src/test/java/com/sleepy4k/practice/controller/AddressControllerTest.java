package com.sleepy4k.practice.controller;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.entity.Address;
import com.sleepy4k.practice.entity.Contact;
import com.sleepy4k.practice.security.BCrypt;
import com.sleepy4k.practice.model.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepy4k.practice.model.AddressResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sleepy4k.practice.repository.UserRepository;
import com.sleepy4k.practice.model.CreateAddressRequest;
import com.sleepy4k.practice.model.UpdateAddressRequest;
import com.sleepy4k.practice.repository.AddressRepository;
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
public class AddressControllerTest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    addressRepository.deleteAll();
    contactRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("user");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setName("User");
    user.setToken("token");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);

    userRepository.save(user);

    Contact contact = new Contact();
    contact.setId("contactId");
    contact.setFirstName("First");
    contact.setLastName("Last");
    contact.setEmail("test@example.com");
    contact.setPhone("0812345678");
    contact.setUser(user);

    contactRepository.save(contact);
  }

  @AfterEach
  void tearDown() {
    addressRepository.deleteAll();
    contactRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void testCreateAddressBadRequest() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    CreateAddressRequest request = new CreateAddressRequest();
    request.setCountry("");

    mockMvc.perform(
      post("/api/contacts/contactId/addresses")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testCreateAddressNotFound() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    CreateAddressRequest request = new CreateAddressRequest();
    request.setCountry("Indonesia");

    mockMvc.perform(
      post("/api/contacts/notFound/addresses")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testCreateAddressUnauthorized() throws Exception {
    CreateAddressRequest request = new CreateAddressRequest();
    request.setCountry("Indonesia");

    mockMvc.perform(
      post("/api/contacts/contactId/addresses")
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
  void testCreateAddress() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    CreateAddressRequest request = new CreateAddressRequest();
    request.setStreet("Street");
    request.setCity("City");
    request.setProvince("Province");
    request.setCountry("Indonesia");
    request.setPostalCode("12345");

    mockMvc.perform(
      post("/api/contacts/contactId/addresses")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {});

      assertNull(response.getErrors());
      assertEquals(request.getStreet(), response.getData().getStreet());
      assertEquals(request.getCity(), response.getData().getCity());
      assertEquals(request.getProvince(), response.getData().getProvince());
      assertEquals(request.getCountry(), response.getData().getCountry());
      assertEquals(request.getPostalCode(), response.getData().getPostalCode());

      assertTrue(addressRepository.existsById(response.getData().getId()));
    });
  }

  @Test
  void testGetAddressNotFound() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    mockMvc.perform(
      get("/api/contacts/contactId/addresses/notFound")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetAddressUnauthorized() throws Exception {
    mockMvc.perform(
      get("/api/contacts/contactId/addresses/addressId")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testGetAddress() throws Exception {
    User user = userRepository.findById("user").orElseThrow();
    Contact contact = contactRepository.findById("contactId").orElseThrow();

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setStreet("Street");
    address.setCity("City");
    address.setProvince("Province");
    address.setCountry("Indonesia");
    address.setPostalCode("12345");
    address.setContact(contact);

    addressRepository.save(address);

    mockMvc.perform(
      get("/api/contacts/contactId/addresses/" + address.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {});

      assertNull(response.getErrors());
      assertEquals(address.getId(), response.getData().getId());
      assertEquals(address.getStreet(), response.getData().getStreet());
      assertEquals(address.getCity(), response.getData().getCity());
      assertEquals(address.getProvince(), response.getData().getProvince());
      assertEquals(address.getCountry(), response.getData().getCountry());
      assertEquals(address.getPostalCode(), response.getData().getPostalCode());
    });
  }

  @Test
  void testUpdateAddressBadRequest() throws Exception {
    User user = userRepository.findById("user").orElseThrow();
    Contact contact = contactRepository.findById("contactId").orElseThrow();

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setStreet("Street");
    address.setCity("City");
    address.setProvince("Province");
    address.setCountry("Indonesia");
    address.setPostalCode("12345");
    address.setContact(contact);

    addressRepository.save(address);

    UpdateAddressRequest request = new UpdateAddressRequest();
    request.setCountry("");

    mockMvc.perform(
      put("/api/contacts/contactId/addresses/" + address.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testUpdateAddressNotFound() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    UpdateAddressRequest request = new UpdateAddressRequest();
    request.setCountry("Indonesia");

    mockMvc.perform(
      put("/api/contacts/contactId/addresses/notFound")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testUpdateAddressUnauthorized() throws Exception {
    UpdateAddressRequest request = new UpdateAddressRequest();
    request.setCountry("Indonesia");

    mockMvc.perform(
      put("/api/contacts/contactId/addresses/addressId")
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
  void testUpdateAddress() throws Exception {
    User user = userRepository.findById("user").orElseThrow();
    Contact contact = contactRepository.findById("contactId").orElseThrow();

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setStreet("Street");
    address.setCity("City");
    address.setProvince("Province");
    address.setCountry("Indonesia");
    address.setPostalCode("12345");
    address.setContact(contact);

    addressRepository.save(address);

    UpdateAddressRequest request = new UpdateAddressRequest();
    request.setStreet("Street 2");
    request.setCity("City 2");
    request.setProvince("Province 2");
    request.setCountry("Indonesia 2");
    request.setPostalCode("54321");

    mockMvc.perform(
      put("/api/contacts/contactId/addresses/" + address.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request))
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {});

      assertNull(response.getErrors());
      assertEquals(address.getId(), response.getData().getId());
      assertEquals(request.getStreet(), response.getData().getStreet());
      assertEquals(request.getCity(), response.getData().getCity());
      assertEquals(request.getProvince(), response.getData().getProvince());
      assertEquals(request.getCountry(), response.getData().getCountry());
      assertEquals(request.getPostalCode(), response.getData().getPostalCode());
    });
  }

  @Test
  void testDeleteAddressNotFound() throws Exception {
    User user = userRepository.findById("user").orElseThrow();

    mockMvc.perform(
      delete("/api/contacts/contactId/addresses/notFound")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testDeleteAddressUnauthorized() throws Exception {
    mockMvc.perform(
      delete("/api/contacts/contactId/addresses/addressId")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testDeleteAddress() throws Exception {
    User user = userRepository.findById("user").orElseThrow();
    Contact contact = contactRepository.findById("contactId").orElseThrow();

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setStreet("Street");
    address.setCity("City");
    address.setProvince("Province");
    address.setCountry("Indonesia");
    address.setPostalCode("12345");
    address.setContact(contact);

    addressRepository.save(address);

    mockMvc.perform(
      delete("/api/contacts/contactId/addresses/" + address.getId())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});

      assertNull(response.getErrors());
      assertEquals("OK", response.getData());
    });
  }

  @Test
  void testListAddressesUnauthorized() throws Exception {
    mockMvc.perform(
      get("/api/contacts/contactId/addresses")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
    ).andExpectAll(
      status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<AddressResponse>>>() {});

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testListAddresses() throws Exception {
    User user = userRepository.findById("user").orElseThrow();
    Contact contact = contactRepository.findById("contactId").orElseThrow();

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setStreet("Street");
    address.setCity("City");
    address.setProvince("Province");
    address.setCountry("Indonesia");
    address.setPostalCode("12345");
    address.setContact(contact);

    addressRepository.save(address);

    mockMvc.perform(
      get("/api/contacts/contactId/addresses")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-TOKEN", user.getToken())
    ).andExpectAll(
      status().isOk()
    ).andDo(result -> {
      WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<AddressResponse>>>() {});

      assertNull(response.getErrors());
      assertEquals(1, response.getData().size());
      assertEquals(address.getId(), response.getData().get(0).getId());
      assertEquals(address.getStreet(), response.getData().get(0).getStreet());
      assertEquals(address.getCity(), response.getData().get(0).getCity());
      assertEquals(address.getProvince(), response.getData().get(0).getProvince());
      assertEquals(address.getCountry(), response.getData().get(0).getCountry());
      assertEquals(address.getPostalCode(), response.getData().get(0).getPostalCode());
    });
  }
}
