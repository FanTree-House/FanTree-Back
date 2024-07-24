package com.example.fantreehouse.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    /*      사용예시
    // 회원가입
    SIGN_UP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
    DEACTIVATE_USER_SUCCESS(HttpStatus.OK, "회원탈퇴에 성공하였습니다."),

    // 로그인
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    GET_USER_SUCCESS(HttpStatus.OK, "유저정보 조회에 성공하였습니다.")
    ;*/

    // 로그인
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    GET_USER_SUCCESS(HttpStatus.OK, "유저정보 조회에 성공하였습니다."),
    WITHDRAW_SUCCESS(HttpStatus.OK, "회원탈퇴에 성공하였습니다."),
    SIGNUP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
    //RefreshToken
    UPDATE_TOKEN_SUCCESS_MESSAGE(HttpStatus.OK, "토큰이 재발급되었습니다."),

    // 엔터테이먼트 계정
    ENTERTAINMENT_CREATE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 생성에 성공하였습니다."),
    ENTERTAINMENT_READ_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 조회에 성공하였습니다."),
    ENTERTAINMENT_UPDATAE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 수정에 성공하였습니다."),
    ENTERTAINMENT_DELETE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 삭제에 성공하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
