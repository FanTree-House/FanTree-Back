package com.example.fantreehouse.domain.comment.dto.response;

import com.example.fantreehouse.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String contents;
    private int likeCount;

    public CommentResponseDto(Long id, String contents, int likeCount) {
        this.id = id;
        this.contents = contents;
        this.likeCount = likeCount;
    }

    public static CommentResponseDto of(Comment comment, int commentLikeCount) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContents(),
                commentLikeCount
        );
    }
}
