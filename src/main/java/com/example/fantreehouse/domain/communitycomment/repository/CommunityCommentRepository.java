package com.example.fantreehouse.domain.communitycomment.repository;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    Optional<CommunityComment> findAllById(Long commentId);

    List<CommunityComment> findByCommunityFeedId(Long feedId);

}
