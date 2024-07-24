package com.example.fantreehouse.domain.communityfeed.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityFeedUpdateRequestDto {
    @NotNull
    @Size(min = 1, message = "수정할 내용을 작성해주세요")
    private String contents;
    private String nickname;
    private String post_picture;

}
