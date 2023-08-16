package com.sleepy4k.practice.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchContactRequest {
  private String name;

  private String email;

  private String phone;

  @NotNull
  private Integer page;

  @NotNull
  private Integer size;
}
