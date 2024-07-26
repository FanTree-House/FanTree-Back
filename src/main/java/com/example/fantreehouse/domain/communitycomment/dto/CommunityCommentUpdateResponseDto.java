package com.example.fantreehouse.domain.communitycomment.dto;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class CommunityCommentUpdateResponseDto {

    private String contents;
    private String nickname;

    public CommunityCommentUpdateResponseDto(CommunityComment comment) {
        this.contents = comment.getContents();
        this.nickname = comment.getNickname();
    }
}
