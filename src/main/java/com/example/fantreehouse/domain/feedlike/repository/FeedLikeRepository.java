package com.example.fantreehouse.domain.feedlike.repository;

import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long>{

    List<FeedLike> findAllFeedLikeByFeedId(Long artistFeedId);

    Long countByFeedId(Long id);

    Optional<FeedLike> findByFeedIdAndUserId(Long artistFeedId, Long id);
}
