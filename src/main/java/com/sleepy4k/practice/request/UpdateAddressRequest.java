package com.sleepy4k.practice.request;

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
public class UpdateAddressRequest {
  @NotBlank
  @JsonIgnore
  private String contactId;

  @NotBlank
  @JsonIgnore
  private String addressId;

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
