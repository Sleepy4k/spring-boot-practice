package com.sleepy4k.practice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  private String id;

  private String street;

  private String city;

  private String province;

  private String country;

  @Column(name = "postal_code")
  private String postalCode;

  @ManyToOne
  @JoinColumn(name = "contact_id", referencedColumnName = "id")
  private Contact contact;
}
