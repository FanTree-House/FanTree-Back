package com.example.fantreehouse.domain.feedlike.repository;

import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long>{

    List<FeedLike> findAllFeedLikeByFeedId(Long artistFeedId);

    int countByFeedId(Long id);
}
