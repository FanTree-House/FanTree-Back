package com.example.fantreehouse.domain.product.product.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.product.pickup.entity.PickUp;
import com.example.fantreehouse.domain.product.product.dto.ProductRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String productName;

    @Column
    private Integer stock;

    @Column
    private String type;

    @Column
    private String artist;

    @Column
    @ElementCollection
    private List<String> productPictureUrl;

    @Column
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<PickUp> pickUpList = new ArrayList<>();

    public Product(ProductRequestDto productRequestDto) {
        this.productName = productRequestDto.getProductName();
        this.stock = productRequestDto.getStock();
        this.type = productRequestDto.getType();
        this.artist = productRequestDto.getArtist();
        this.productPictureUrl = new ArrayList<>();
        this.price = productRequestDto.getPrice();
    }

    public void updateProductName(String productName) {
        this.productName = productName;
    }

    public void updateStock(Integer stock) {
        this.stock = stock;
    }

    public void updateType(String type) {
        this.type = type;
    }

    public void updateArtist(String artist) {
        this.artist = artist;
    }

    public void updatePrice(Integer price) {
        this.price = price;
    }

    public void updateImageUrls(List<String> imageUrls) {
        this.productPictureUrl.clear();
        this.productPictureUrl.addAll(imageUrls);
    }
}
