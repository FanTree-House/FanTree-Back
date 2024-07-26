package com.example.fantreehouse.domain.communitycomment.repository;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    List<CommunityComment> findAllByCommunityFeed_Id(Long feedID);

    Optional<CommunityComment> findByUserId(Long commentId);

    Optional<CommunityComment> findAllById(Long commentId);
}
