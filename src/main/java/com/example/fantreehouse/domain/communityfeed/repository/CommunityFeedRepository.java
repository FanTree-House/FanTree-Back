package com.example.fantreehouse.domain.communityfeed.repository;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityFeedRepository extends JpaRepository<CommunityFeed, Long> {
    List<CommunityFeed> findAllUserId(Long userId);
}
