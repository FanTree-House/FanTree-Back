package com.example.fantreehouse.domain.enterfeed.repository;

import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnterFeedRepository extends JpaRepository<EnterFeed, Long> {

    Optional<EnterFeed> findByIdAndArtistGroupGroupNameAndCategory(Long id, String groupName, FeedCategory category);

    List<EnterFeed> findAllByEntertainmentEnterNameAndCategory(String enterName, FeedCategory category);
}