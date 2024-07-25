package com.example.fantreehouse.domain.enterfeed.repository;

import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnterFeedRepository extends JpaRepository<EnterFeed, Long> {

    // 아티스트 그룹 이름과 카테고리로 엔터피드를 찾기
    Optional<EnterFeed> findByIdAndArtistGroupGroupNameAndCategory(Long id, String groupName, FeedCategory category);

    // 아티스트 그룹 이름과 카테고리로 모든 엔터피드를 찾기
    List<EnterFeed> findAllByArtistGroupGroupNameAndCategory(String groupName, FeedCategory category);
}