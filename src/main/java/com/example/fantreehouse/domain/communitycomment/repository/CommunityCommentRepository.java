package com.example.fantreehouse.domain.communitycomment.repository;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
