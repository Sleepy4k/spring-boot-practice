package com.sleepy4k.practice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sleepy4k.practice.entity.Address;
import com.sleepy4k.practice.entity.Contact;

public interface AddressRepository extends JpaRepository<Address, String> {
  Optional<Address> findFirstByContactAndId(Contact contact, String id);

  List<Address> findAllByContact(Contact contact);
}
