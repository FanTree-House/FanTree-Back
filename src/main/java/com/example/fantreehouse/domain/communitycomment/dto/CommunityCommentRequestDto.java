package com.example.fantreehouse.domain.communitycomment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class CommunityCommentRequestDto {

    @NotNull
    @Size(min = 1, message = "수정할 내용을 작성해주세요")
    private String contents;

}
