package com.example.fantreehouse.domain.communityfeed.repository;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityFeedRepository extends JpaRepository<CommunityFeed, Long> {

   Optional<List<CommunityFeed>> findAllByUserId(Long userId);

}
