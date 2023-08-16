package com.sleepy4k.practice.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactResponse {
  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;
}
