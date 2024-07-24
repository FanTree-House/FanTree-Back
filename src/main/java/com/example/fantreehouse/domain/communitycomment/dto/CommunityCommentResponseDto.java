package com.example.fantreehouse.domain.communitycomment.dto;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import lombok.Getter;

@Getter
public class CommunityCommentResponseDto {

    private String contents;


    public CommunityCommentResponseDto(CommunityComment comment) {
        this.contents = comment.getContents();

    }
}

