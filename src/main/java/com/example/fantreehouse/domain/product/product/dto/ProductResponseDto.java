package com.example.fantreehouse.domain.product.product.dto;

import com.example.fantreehouse.domain.product.product.entity.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponseDto {
    private Long id;
    private String productName;
    private Integer stock;
    private String type;
    private String artist;
    private List<String> productPictureUrl;
    private Integer price;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.stock = product.getStock();
        this.type = product.getType();
        this.artist = product.getArtist();
        this.productPictureUrl = product.getProductPictureUrl();
        this.price = product.getPrice();
    }
}
