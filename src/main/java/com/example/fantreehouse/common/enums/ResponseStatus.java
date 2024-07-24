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

    // 아티스트 그룹
    ARTIST_GROUP_CREATE_SUCCESS(HttpStatus.OK, "아티스트 그룹 생성에 성공하였습니다."),
    ARTIST_GROUP_RETRIEVE_SUCCESS(HttpStatus.OK, "아티스트 그룹 조회에 성공하였습니다."),
    ARTIST_GROUP_UPDATE_SUCCESS(HttpStatus.OK, "아티스트 그룹 수정에 성공하였습니다."),
    ARTIST_GROUP_DELETE_SUCCESS(HttpStatus.OK, "아티스트 그룹 삭제에 성공하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}