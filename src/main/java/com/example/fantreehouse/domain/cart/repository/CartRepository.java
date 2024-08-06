package com.example.fantreehouse.domain.cart.repository;

import com.example.fantreehouse.domain.cart.entity.Cart;
import com.example.fantreehouse.domain.product.product.entity.Product;
import com.example.fantreehouse.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  List<Cart> findByUser(User user);

  Optional<Cart> findByUserAndProduct(User user, Product product);

  List<Cart> findAllByUserId(Long id);

  int countByUserId(Long orderId);
}
