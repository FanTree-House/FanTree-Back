package com.example.fantreehouse.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartItemDto {

  private String product;
  private Long price;
  private int count;
  private String imageUrl;
  private  Long productDetailId;

  public CartItemDto(String product, Long price, int count, String imageUrl, Long productDetailId) {
    this.product = product;
    this.price = price;
    this.count = count;
    this.imageUrl = imageUrl;
    this.productDetailId = productDetailId;
  }

}
