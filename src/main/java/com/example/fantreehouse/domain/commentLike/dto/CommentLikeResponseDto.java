package com.example.fantreehouse.domain.commentLike.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CommentLikeResponseDto {
    private Boolean isLiked;
    private Long likeCount;

    public CommentLikeResponseDto(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public CommentLikeResponseDto(Long likeCount) {
        this.likeCount = likeCount;
    }
}
