package com.sleepy4k.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sleepy4k.practice.entity.User;
import com.sleepy4k.practice.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {
  Optional<Contact> findFirstByUserAndId(User user, String id);
}
