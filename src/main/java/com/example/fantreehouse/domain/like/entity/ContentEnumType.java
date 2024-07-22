package com.example.fantreehouse.domain.like.entity;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ContentEnumType {
    // 게시글 또는 댓글
    POST("post"),
    COMMENT("comment");

    private final String type;

    ContentEnumType(String type) {
        this.type = type;
    }

    public static ContentEnumType getByType(String type) {
        if (Objects.equals(type, POST.type)) {
            return POST;
        } else if (Objects.equals(type, COMMENT.type)) {
            return COMMENT;
        } else
            throw new MismatchException(ErrorType.CONTENT_TYPE_MISMATCH);
    }
}
