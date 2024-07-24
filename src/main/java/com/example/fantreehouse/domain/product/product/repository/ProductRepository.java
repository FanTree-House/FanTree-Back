package com.example.fantreehouse.domain.product.product.repository;

import com.example.fantreehouse.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
