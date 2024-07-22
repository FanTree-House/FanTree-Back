package com.example.fantreehouse.domain.merch.product.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.merch.pickup.entity.PickUp;
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
    private Long stock;

    @Column
    private String type;

    @Column
    private String artist;

    @Column
    private String product_picture;

    @Column
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<PickUp> pickUpList = new ArrayList<>();

}
