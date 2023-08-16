package com.sleepy4k.practice.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
  private Integer currentPage;

  private Integer totalPage;

  private Integer size;
}
