package com.example.fantreehouse.domain.comment.repository;

import com.example.fantreehouse.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByFeedId(Long feedId, PageRequest pageRequest);
}
