package com.example.fantreehouse.domain.cart.entity;

import com.example.fantreehouse.domain.product.product.entity.Product;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart")
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long count = 0L;

  @Column(nullable = false)
  private Long totalPrice = 0L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Builder
  public Cart(Long count, Long totalPrice, User user, Product productDetail) {
    this.count = count;
    this.totalPrice = totalPrice;
    this.user = user;
    this.product = productDetail;
  }

  // 상품 수량 증가
  public void incrementCount(Long count) {
    this.count += count;
  }

  // totalPrice 증가
  public void incrementTotalPrice(Long price) {
    this.totalPrice += price;
  }

  public void updateCountPrice(Long newCount) {
    this.count = newCount;
    this.totalPrice = newCount * this.product.getPrice();
  }

  public void setProductDetail(Product product) {
    this.product = product;
    this.totalPrice = this.count * product.getPrice();
  }

}
