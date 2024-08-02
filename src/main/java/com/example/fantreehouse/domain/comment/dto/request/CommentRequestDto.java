package com.example.fantreehouse.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "등록할 댓글을 입력하세요.")
    private String contents;
}
