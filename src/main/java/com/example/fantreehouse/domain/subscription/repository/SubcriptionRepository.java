package com.example.fantreehouse.domain.subscription.repository;

import com.example.fantreehouse.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubcriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserId(Long userId);
}
