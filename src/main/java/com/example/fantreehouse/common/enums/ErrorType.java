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
    CONTENT_TYPE_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "컨텐츠 타입이 일치하지 않습니다."),

    //user
    USER_NOT_FOUND(HttpStatus.OK, "유저를 찾을 수 없습니다."),
    MISMATCH_PASSWORD(HttpStatus.OK, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_ID(HttpStatus.OK, "중복된 아이디입니다."),
    BLACKLIST_EMAIL(HttpStatus.OK, "블랙리스트에 등록된 이메일로 가입할 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.OK, "중복된 닉네임입니다."),
    WITHDRAW_USER(HttpStatus.NOT_FOUND,
        "탈퇴한 회원입니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.NOT_FOUND,
        "REFRESH_TOKEN 값이 일치 하지 않습니다."),
    MISMATCH_ADMINTOKEN(HttpStatus.OK, "Admin 토큰값이 일치하지 않습니다."),
    MISMATCH_ARTISTTOKEN(HttpStatus.OK, "Artist 토큰값이 일치하지 않습니다."),
    MISMATCH_ENTERTAINMENTTOKEN(HttpStatus.OK, "Entertainment 토큰값이 일치하지 않습니다."),

    // Entertainment
    NOT_FOUND_ENTER(HttpStatus.NOT_FOUND, "존재하지 않는 엔터테인먼트입니다."),

    // 엔터테인먼트 및 아티스트 관련 에러
    ENTERTAINMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "엔터테인먼트를 찾을 수 없습니다."),
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트를 찾을 수 없습니다."),
    ARTIST_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "아티스트 그룹을 찾을 수 없습니다."),

    //유저 커뮤니티 피드

    NOT_FOUND_FEED(HttpStatus.NOT_FOUND, "피드를 찾을수 없습니다"),
    NOT_USER_FEED(HttpStatus.NOT_ACCEPTABLE, "유저가 작성한 피드가 아닙니다."),
    NOT_MATCH_USER(HttpStatus.NOT_ACCEPTABLE, "해당 유저는 아티스트그룹을 구독하지 않았습니다."),

    // 유저 커뮤니티 피드 댓글
    NOT_FOUNT_COMMENT(HttpStatus.NOT_FOUND, "댓글을 찾지 못했습니다."),
    NOT_USER_COMMENT(HttpStatus.NOT_ACCEPTABLE, "유저가 작성한 댓글이 아닙니다."),

    //아티스트그룹
    NOT_FOUND_ARTISTGROUP(HttpStatus.NOT_FOUND, "아티스트그룹을 찾지 못했습니다."),


    ;



    private final HttpStatus httpStatus;
    private final String message;
}