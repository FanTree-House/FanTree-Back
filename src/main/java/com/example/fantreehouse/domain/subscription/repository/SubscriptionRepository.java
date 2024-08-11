package com.example.fantreehouse.domain.subscription.repository;

import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<List<Subscription>> findAllByUserId(Long userId);

    Optional<Subscription> findByUserIdAndArtistGroupId(Long userId, Long groupId);
    Optional<Subscription> findByUserId(Long UserId);

    Optional<List<Subscription>> findByUser(User user);

    @Query("SELECT a.artistGroup FROM Subscription a " +
            "GROUP BY a.artistGroup " +
            "ORDER BY COUNT(a) DESC")
    List<Long> findMostDataArtistGroups();
}
