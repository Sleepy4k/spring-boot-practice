package com.sleepy4k.practice.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {
  @NotBlank
  @JsonIgnore
  private String contactId;

  @Size(max = 200)
  private String street;

  @Size(max = 100)
  private String city;

  @Size(max = 100)
  private String province;

  @NotBlank
  @Size(max = 100)
  private String country;

  @Size(max = 100)
  private String postalCode;
}
