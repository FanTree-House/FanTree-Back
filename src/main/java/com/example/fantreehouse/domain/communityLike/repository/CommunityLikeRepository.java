package com.example.fantreehouse.domain.communityLike.repository;

import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    Optional <CommunityLike> findByUserIdAndCommunityFeedId(Long userId, Long feedId);

    Optional <CommunityLike> findByUserId(Long userId);

    Optional <CommunityLike> findByUserIdAndCommunityCommentId(Long userId, Long feedId);
}
