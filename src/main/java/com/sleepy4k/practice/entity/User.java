package com.sleepy4k.practice.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
  @Id
  private String username;

  private String password;

  private String name;

  private String token;

  @Column(name = "token_expiration")
  private Long tokenExpiredAt;

  @OneToMany(mappedBy = "user")
  private List<Contact> contacts;
}
