package com.example.fantreehouse.domain.subscription.repository;

import com.example.fantreehouse.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcriptionRepository extends JpaRepository<Subscription, Long> {
}
