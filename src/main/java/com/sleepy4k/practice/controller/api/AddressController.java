package com.sleepy4k.practice.controller.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.model.WebResponse;
import com.sleepy4k.practice.model.AddressResponse;
import com.sleepy4k.practice.service.api.AddressService;
import com.sleepy4k.practice.request.CreateAddressRequest;
import com.sleepy4k.practice.request.UpdateAddressRequest;

@RestController
@Component("APIAddressController")
@RequestMapping("/api/contacts/{id_contact}/addresses")
public class AddressController {
  @Autowired
  private AddressService addressService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request, @PathVariable("id_contact") String contactId) {
    request.setContactId(contactId);

    AddressResponse addressResponse = addressService.create(user, request);

    return WebResponse.<AddressResponse>builder().data(addressResponse).build();
  }

  @GetMapping(path = "/{id_address}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AddressResponse> get(User user, @PathVariable("id_contact") String contactId, @PathVariable("id_address") String addressId) {
    AddressResponse addressResponse = addressService.get(user, contactId, addressId);

    return WebResponse.<AddressResponse>builder().data(addressResponse).build();
  }

  @PutMapping(path = "/{id_address}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<AddressResponse> update(User user, @RequestBody UpdateAddressRequest request, @PathVariable("id_contact") String contactId, @PathVariable("id_address") String addressId) {
    request.setContactId(contactId);
    request.setAddressId(addressId);

    AddressResponse addressResponse = addressService.update(user, request);

    return WebResponse.<AddressResponse>builder().data(addressResponse).build();
  }

  @DeleteMapping(path = "/{id_address}", produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<String> remove(User user, @PathVariable("id_contact") String contactId, @PathVariable("id_address") String addressId) {
    addressService.remove(user, contactId, addressId);

    return WebResponse.<String>builder().data("OK").build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WebResponse<List<AddressResponse>> list(User user, @PathVariable("id_contact") String contactId) {
    List<AddressResponse> addressResponses = addressService.list(user, contactId);

    return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
  }
}
