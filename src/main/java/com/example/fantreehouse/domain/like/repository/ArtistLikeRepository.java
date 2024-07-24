package com.example.fantreehouse.domain.like.repository;

import com.example.fantreehouse.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistLikeRepository extends JpaRepository<Like, Long> {
}
