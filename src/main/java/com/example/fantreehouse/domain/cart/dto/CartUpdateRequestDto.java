package com.example.fantreehouse.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

  private Long productId;

  private int count;

  public CartUpdateRequestDto(Long productId, int count) {
    this.productId = productId;
    this.count = count;
  }

}
