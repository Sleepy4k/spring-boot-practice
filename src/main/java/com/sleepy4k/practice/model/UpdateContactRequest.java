package com.sleepy4k.practice.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateContactRequest {
  @NotBlank
  @JsonIgnore
  private String id;

  @NotBlank
  @Size(max = 100)
  private String firstName;

  @Size(max = 100)
  private String lastName;

  @Email
  @Size(max = 100)
  private String email;

  @Size(max = 100)
  private String phone;
}
