package com.example.fantreehouse.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartDeleteRequestDto {

  private Long productDetailId;

  public CartDeleteRequestDto(Long productDetailId) {
    this.productDetailId = productDetailId;
  }

}
