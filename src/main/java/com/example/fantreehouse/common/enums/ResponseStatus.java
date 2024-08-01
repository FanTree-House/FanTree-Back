package com.example.fantreehouse.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    // User
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    GET_USER_SUCCESS(HttpStatus.OK, "유저정보 조회에 성공하였습니다."),
    UNIQUE_ID(HttpStatus.OK, "사용할 수 있는 ID입니다."),
    UNIQUE_NICKNAME(HttpStatus.OK, "사용할 수 있는 닉네임입니다."),

    // 구독자 커뮤니티 피드
    CREATE_SUCCESS_FEED(HttpStatus.OK, "게시글이 생성되었습니다."),
    USER_COMMUNITY_UPDATE_SUCCESS(HttpStatus.OK, "게시글이 수정되었습니다."),
    USER_COMMUNITY_DELETE_SUCCESS(HttpStatus.OK, "게시글이 삭제되었습니다."),

    // 구독자 커뮤니티와 아티스트피드의 댓글
    CREATE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 생성되었습니다."),
    UPDATE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 수정되었습니다."),
    DELETE_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 삭제되었습니다."),
    READ_SUCCESS_COMMENT(HttpStatus.OK, "댓글이 조회되었습니다."),
    // 좋아요
    SUCCESS_FEED_LIKE(HttpStatus.OK,"피드를 좋아합니다!" ),
    DELETE_FEED_LIKE(HttpStatus.OK, "피드의 좋아요를 취소합니다."),
    SUCCESS_COMMENT_LIKE(HttpStatus.OK,"댓글을 좋아합니다!" ),
    DELETE_COMMENT_LIKE(HttpStatus.OK, "댓글의 좋아요를 취소합니다."),

    //프로필
    WITHDRAW_SUCCESS(HttpStatus.OK, "회원탈퇴에 성공하였습니다."),
    SIGNUP_SUCCESS(HttpStatus.OK, "회원가입에 성공하였습니다."),
    PROFILE_UPDATE(HttpStatus.OK, "프로필이 변경되었습니다."),

     //구독활성, 비활성화
    USER_SUCCESS_SUBSCRIPT(HttpStatus.OK, "해당 그룹을 구독하였습니다."),
    USER_DELETE_SUBSCRIPT(HttpStatus.OK, "해당 그룹의 구독을 해지했습니다."),

    //RefreshToken
    UPDATE_TOKEN_SUCCESS_MESSAGE(HttpStatus.OK, "토큰이 재발급되었습니다."),

    // 엔터테이먼트 계정
    ENTERTAINMENT_CREATE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 생성에 성공하였습니다."),
    ENTERTAINMENT_READ_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 조회에 성공하였습니다."),
    ENTERTAINMENT_UPDATAE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 수정에 성공하였습니다."),
    ENTERTAINMENT_DELETE_SUCCESS(HttpStatus.OK,  "엔터테인먼트 계정 삭제에 성공하였습니다."),

    // 아티스트
    ARTIST_CREATED(HttpStatus.CREATED, "아티스트 등록에 성공하였습니다."),
    ARTIST_UPDATED(HttpStatus.OK, "아티스트 프로필이 수정되었습니다."),
    ARTIST_READ_SUCCESS(HttpStatus.OK, "아티스트가 조회되었습니다."),
    ARTIST_DELETED(HttpStatus.OK, "아티스트 계정이 삭제되었습니다."),

    // 아티스트 그룹
    ARTIST_GROUP_CREATE_SUCCESS(HttpStatus.OK, "아티스트 그룹 생성에 성공하였습니다."),
    ARTIST_GROUP_RETRIEVE_SUCCESS(HttpStatus.OK, "아티스트 그룹 조회에 성공하였습니다."),
    ARTIST_GROUP_UPDATE_SUCCESS(HttpStatus.OK, "아티스트 그룹 수정에 성공하였습니다."),
    ARTIST_REMOVE_SUCCESS(HttpStatus.OK, "그룹에서 아티스트 삭제를 성공하였습니다."),
    ARTIST_GROUP_DELETE_SUCCESS(HttpStatus.OK, "아티스트 그룹 삭제에 성공하였습니다."),

    //Feed
    FEED_CREATED(HttpStatus.CREATED, "글이 등록되었습니다."),
    FEED_UPDATED(HttpStatus.OK, "수정되었습니다."),
    FEED_READ_SUCCESS(HttpStatus.OK, "조회되었습니다."),
    FEED_DELETED(HttpStatus.OK, "삭제되었습니다."),

    //아티스트 Feed/comment Like
    FEED_LIKE_CHANGED(HttpStatus.OK, "Like 상태가 변경되었습니다."),
    COMMENT_LIKE_CHANGED(HttpStatus.OK, "Like 상태가 변경되었습니다."),

    // 엔터 피드
    NOTICE_CREATE_SUCCESS(HttpStatus.OK, "공지사항 생성에 성공하였습니다."),
    NOTICE_RETRIEVE_SUCCESS(HttpStatus.OK, "공지사항 조회에 성공하였습니다."),
    NOTICE_UPDATE_SUCCESS(HttpStatus.OK, "공지사항 수정에 성공하였습니다."),
    NOTICE_DELETE_SUCCESS(HttpStatus.OK, "공지사항 삭제에 성공하였습니다."),
    SCHEDULE_CREATE_SUCCESS(HttpStatus.OK, "스케줄 생성에 성공하였습니다."),
    SCHEDULE_RETRIEVE_SUCCESS(HttpStatus.OK, "스케줄 조회에 성공하였습니다."),
    SCHEDULE_UPDATE_SUCCESS(HttpStatus.OK, "스케줄 수정에 성공하였습니다."),
    SCHEDULE_DELETE_SUCCESS(HttpStatus.OK, "스케줄 삭제에 성공하였습니다."),

    // 상품
    PRODUCT_CREATE_SUCCESS(HttpStatus.OK, "상품 등록에 성공하였습니다."),
    PRODUCT_READ_SUCCESS(HttpStatus.OK, "상품 조회에 성공하였습니다."),
    PRODUCT_UPDATE_SUCCESS(HttpStatus.OK, "상품 수정에 성공하였습니다."),
    PRODUCT_DELETE_SUCCESS(HttpStatus.OK, "상품 삭제에 성공하였습니다."),

    // 어드민 권한
    TRANSFORM_BLACKLIST_SUCCESS(HttpStatus.OK, "블랙리스트 전환에 성공하였습니다."),
    USER_ROLE_UPDATE_SUCCESS(HttpStatus.OK, "회원권환 수정에 성공하였습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}