package com.example.fantreehouse.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartRequestDto {

  private Long productId;

  private int count;

  public CartRequestDto(Long productId, int count) {
    this.productId = productId;
    this.count = count;
  }
}
