package com.example.fantreehouse.domain.enterfeed.repository;

import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnterFeedRepository extends JpaRepository<EnterFeed, Long> {
    Optional<EnterFeed> findByFeedIdAndEntertainmentEnterName(String feedId, String groupName);
    List<EnterFeed> findAllByEntertainmentEnterNameAndCategory(String groupName, String category);
}