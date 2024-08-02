package com.example.fantreehouse.domain.subscription.repository;

import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<List<Subscription>> findAllByUserId(Long userId);

    Optional<Subscription> findByUser_IdAndArtistGroup_Id(Long userId, Long groupId);
    Optional<Subscription> findByUserId(Long UserId);

    Optional<List<Subscription>> findByUser(User user);
}
