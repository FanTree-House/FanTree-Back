package com.example.fantreehouse.domain.feed.repository;

import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findByArtistGroup(ArtistGroup artistGroup, PageRequest pageRequest);

    List<Feed> findByArtistGroup(ArtistGroup artistGroup);
    List<Feed> findByArtistGroupId(Long artistGroupId);

    @Query("SELECT f FROM Feed f LEFT JOIN FETCH f.imageUrls WHERE f.id = :id")
    Optional<Feed> findById(Long id);
}
