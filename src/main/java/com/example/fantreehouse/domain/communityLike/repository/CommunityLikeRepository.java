package com.example.fantreehouse.domain.communityLike.repository;

import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
}
