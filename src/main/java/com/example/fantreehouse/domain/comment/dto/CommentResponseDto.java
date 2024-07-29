package com.example.fantreehouse.domain.comment.dto;

import com.example.fantreehouse.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String contents;
    private int likeCount;
    private String writer;

    public CommentResponseDto(String contents, int likeCount, String writer) {
        this.contents = contents;
        this.likeCount = likeCount;
        this.writer = writer; //유저의 경우 nickname, 아티스트의 경우 artistName(활동명)
    }

    public static CommentResponseDto of(Comment comment) {

        return new CommentResponseDto(
                comment.getContents(),
                comment.getLikesCount(),
                comment.getUser().getNickname()
        );
    }
}
