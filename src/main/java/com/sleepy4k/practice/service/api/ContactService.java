package com.sleepy4k.practice.service.api;

import java.util.List;
import java.util.UUID;
import java.util.Objects;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.entity.Contact;
import com.sleepy4k.practice.model.ContactResponse;
import com.sleepy4k.practice.repository.ContactRepository;
import com.sleepy4k.practice.request.CreateContactRequest;
import com.sleepy4k.practice.request.SearchContactRequest;
import com.sleepy4k.practice.request.UpdateContactRequest;
import com.sleepy4k.practice.service.ValidationService;

import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.Predicate;

@Service
public class ContactService {
  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ValidationService validationService;

  private ContactResponse mapContactToContactResponse(Contact contact) {
    return ContactResponse.builder()
      .id(contact.getId())
      .firstName(contact.getFirstName())
      .lastName(contact.getLastName())
      .email(contact.getEmail())
      .phone(contact.getPhone())
      .build();
  }

  @Transactional
  public ContactResponse create(User user, CreateContactRequest request) {
    validationService.validate(request);

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setFirstName(request.getFirstName());
    contact.setLastName(request.getLastName());
    contact.setEmail(request.getEmail());
    contact.setPhone(request.getPhone());
    contact.setUser(user);

    contactRepository.save(contact);

    return mapContactToContactResponse(contact);
  }

  @Transactional
  public ContactResponse get(User user, String id) {
    Contact contact = contactRepository.findFirstByUserAndId(user, id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    return mapContactToContactResponse(contact);
  }

  @Transactional
  public ContactResponse update(User user, UpdateContactRequest request) {
    validationService.validate(request);

    Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    contact.setFirstName(request.getFirstName());
    contact.setLastName(request.getLastName());
    contact.setEmail(request.getEmail());
    contact.setPhone(request.getPhone());

    contactRepository.save(contact);

    return mapContactToContactResponse(contact);
  }

  @Transactional
  public void delete(User user, String id) {
    Contact contact = contactRepository.findFirstByUserAndId(user, id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found."));

    contactRepository.delete(contact);
  }

  @Transactional
  public Page<ContactResponse> search(User user, SearchContactRequest request) {
    Specification<Contact> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      predicates.add(builder.equal(root.get("user"), user));

      if (Objects.nonNull(request.getName())) {
        predicates.add(builder.or(
          builder.like(root.get("firstName"), "%" + request.getName() + "%"),
          builder.like(root.get("lastName"), "%" + request.getName() + "%")
        ));
      }

      if (Objects.nonNull(request.getEmail())) {
        predicates.add(builder.like(root.get("email"), "%" + request.getEmail() + "%"));
      }

      if (Objects.nonNull(request.getPhone())) {
        predicates.add(builder.like(root.get("phone"), "%" + request.getPhone() + "%"));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<Contact> contacts =  contactRepository.findAll(specification, pageable);
    List<ContactResponse> contactResponses = contacts.getContent().stream()
      .map(this::mapContactToContactResponse)
      .toList();

    return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
  }
}
