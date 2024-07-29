package com.example.fantreehouse.domain.comment.dto.response;

import com.example.fantreehouse.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String contents;
    private int likeCount;

    public CommentResponseDto(String contents, int likeCount) {
        this.contents = contents;
        this.likeCount = likeCount;
    }

    public static CommentResponseDto of(Comment comment, int commentLikeCount) {
        return new CommentResponseDto(
                comment.getContents(),
                commentLikeCount
        );
    }
}
