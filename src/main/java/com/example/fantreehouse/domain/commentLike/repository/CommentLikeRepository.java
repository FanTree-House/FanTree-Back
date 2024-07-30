package com.example.fantreehouse.domain.commentLike.repository;

import com.example.fantreehouse.domain.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findAllCommentLikeByCommentId(Long commentId);
}
