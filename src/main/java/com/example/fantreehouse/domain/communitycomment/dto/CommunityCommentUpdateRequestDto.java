package com.example.fantreehouse.domain.communitycomment.dto;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class CommunityCommentUpdateRequestDto {

    @NotNull
    @Size(min = 1, message = "수정할 댓글을 입력해주세요")
    private String contents;

    public CommunityCommentUpdateRequestDto(CommunityComment comment) {
        this.contents = comment.getContents();}
}

