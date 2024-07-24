package com.example.fantreehouse.domain.feed.repository;

import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findByArtistGroup(ArtistGroup artistGroup, PageRequest pageRequest);
}
