package com.sleepy4k.practice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.entity.Address;
import com.sleepy4k.practice.entity.Contact;
import com.sleepy4k.practice.model.AddressResponse;
import com.sleepy4k.practice.model.CreateAddressRequest;
import com.sleepy4k.practice.model.UpdateAddressRequest;
import com.sleepy4k.practice.repository.AddressRepository;
import com.sleepy4k.practice.repository.ContactRepository;

import jakarta.transaction.Transactional;

@Service
public class AddressService {
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private ValidationService validationService;

  private AddressResponse mapAddressToAddressResponse(Address address) {
    return AddressResponse.builder()
      .id(address.getId())
      .street(address.getStreet())
      .city(address.getCity())
      .province(address.getProvince())
      .country(address.getCountry())
      .postalCode(address.getPostalCode())
      .build();
  }

  @Transactional
  public AddressResponse create(User user, CreateAddressRequest request) {
    validationService.validate(request);

    Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    Address address = new Address();
    address.setId(UUID.randomUUID().toString());
    address.setContact(contact);
    address.setStreet(request.getStreet());
    address.setCity(request.getCity());
    address.setProvince(request.getProvince());
    address.setCountry(request.getCountry());
    address.setPostalCode(request.getPostalCode());

    addressRepository.save(address);

    return mapAddressToAddressResponse(address);
  }

  @Transactional
  public AddressResponse get(User user, String contactId, String addressId) {
    Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    Address address = addressRepository.findFirstByContactAndId(contact, addressId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found."));

    return mapAddressToAddressResponse(address);
  }

  @Transactional
  public AddressResponse update(User user, UpdateAddressRequest request) {
    validationService.validate(request);

    Contact contact = contactRepository.findFirstByUserAndId(user, request.getContactId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    Address address = addressRepository.findFirstByContactAndId(contact, request.getAddressId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found."));
    
    address.setStreet(request.getStreet());
    address.setCity(request.getCity());
    address.setProvince(request.getProvince());
    address.setCountry(request.getCountry());
    address.setPostalCode(request.getPostalCode());

    addressRepository.save(address);

    return mapAddressToAddressResponse(address);
  }

  @Transactional
  public void remove(User user, String contactId, String addressId) {
    Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    Address address = addressRepository.findFirstByContactAndId(contact, addressId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found."));

    addressRepository.delete(address);
  }

  @Transactional
  public List<AddressResponse> list(User user, String contactId) {
    Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    List<Address> addresses = addressRepository.findAllByContact(contact);

    return addresses.stream().map(this::mapAddressToAddressResponse).toList();
  }
}
