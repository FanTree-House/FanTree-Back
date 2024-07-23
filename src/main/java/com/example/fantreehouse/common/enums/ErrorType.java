package com.example.fantreehouse.common.enums;

import com.example.fantreehouse.common.exception.errorcode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType implements ErrorCode {
    /*    사용예시
    //user
    DUPLICATE_EMAIL(HttpStatus.LOCKED, "이미 존재하는 이메일입니다."),
    DEACTIVATE_USER(HttpStatus.LOCKED, "탈퇴한 회원입니다."),
    INVALID_PASSWORD(HttpStatus.LOCKED, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.LOCKED, "존재하지 않는 회원입니다."),
    NOT_AVAILABLE_PERMISSION(HttpStatus.LOCKED, "권한이 없습니다.")
     */

    // like
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요가 없습니다."),
    SELF_LIKE(HttpStatus.NOT_ACCEPTABLE, "자신의 글/댓글에 좋아요 사용 불가"),
    DUPLICATE_LIKE(HttpStatus.NOT_ACCEPTABLE, "좋아요는 한번만 가능합니다."),
    USER_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "유저와 좋아요가 일치하지 않습니다."),
    CONTENT_TYPE_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "컨텐츠 타입이 일치하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
