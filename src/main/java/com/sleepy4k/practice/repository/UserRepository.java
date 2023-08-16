package com.sleepy4k.practice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sleepy4k.practice.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findFirstByToken(String token);
}
