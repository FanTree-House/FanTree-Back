package com.example.fantreehouse.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateFeedRequestDto {

    String artistName;

    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(max = 255, message = "입력가능한 글자수를 초과하였습니다.")
    String contents;

    String post_picture;

}
