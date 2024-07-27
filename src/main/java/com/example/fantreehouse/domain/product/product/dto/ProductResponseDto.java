package com.example.fantreehouse.domain.product.product.dto;

import com.example.fantreehouse.domain.product.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private String productName;
    private Integer stock;
    private String type;
    private String artist;
    private String productPicture;
    private Integer price;

    public ProductResponseDto(Product product) {
        this.productName = product.getProductName();
        this.stock = product.getStock();
        this.type = product.getType();
        this.artist = product.getArtist();
        this.productPicture = product.getProductPicture();
        this.price = product.getPrice();
    }
}
