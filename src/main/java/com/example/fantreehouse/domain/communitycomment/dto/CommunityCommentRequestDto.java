package com.example.fantreehouse.domain.communitycomment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class CommunityCommentRequestDto {

    @NotNull
    @Size(min = 1, message = "작성할 댓글을 입력해주세요")
    private String contents;
}
