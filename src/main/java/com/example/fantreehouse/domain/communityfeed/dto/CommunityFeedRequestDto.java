package com.example.fantreehouse.domain.communityfeed.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityFeedRequestDto {

    @Size(min = 1, message = "작성할 내용을 입력해주세요")
    private String contents;
    private String post_picture;

}
