package com.example.fantreehouse.domain.product.pickup.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.product.product.entity.Product;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "pickup")
public class PickUp extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate reservationDate;

    @Column(length = 20)
    private String reservationNumber;

    @Column
    private LocalDate pickUpDate;

    // 사용자랑 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // MD 상품이랑 다대일
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
