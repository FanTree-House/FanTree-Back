package com.example.fantreehouse.domain.comment.dto.response;

import com.example.fantreehouse.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String contents;
    private int likeCount;
    private String nickName;
    private String profileImageUrl;

    public CommentResponseDto(Long id, String contents, int likeCount, String nickName, String profileImageUrl) {
        this.id = id;
        this.contents = contents;
        this.likeCount = likeCount;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
    }

    public static CommentResponseDto of(Comment comment, int commentLikeCount, String nickName, String profileImageUrl) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContents(),
                commentLikeCount,
                nickName,
                profileImageUrl
        );
    }
}
