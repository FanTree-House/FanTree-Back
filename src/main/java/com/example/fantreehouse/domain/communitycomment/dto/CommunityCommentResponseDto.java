package com.example.fantreehouse.domain.communitycomment.dto;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import lombok.Getter;

@Getter
public class CommunityCommentResponseDto {

    private String contents;
    private String nickname;


    public CommunityCommentResponseDto(CommunityComment comment) {
        this.contents = comment.getContents();
        this.nickname = comment.getNickname();
    }
}

