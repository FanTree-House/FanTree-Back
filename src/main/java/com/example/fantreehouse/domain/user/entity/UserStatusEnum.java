package com.example.fantreehouse.domain.user.entity;

public enum UserStatusEnum {

    ACTIVE_USER(Status.USER), //일반 유저
    WITHDRAW_USER(Status.WITHDRAW_USER), //탈퇴한 유저
    BLACK_LIST(Status.BLACK_LIST), //블랙리스트
    INACTIVE_USER(Status.INACTIVE_USER); //휴면 유저

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String USER = "USER";
        public static final String WITHDRAW_USER = "NON_USER";
        public static final String BLACK_LIST = "BLACK_LIST";
        public static final String INACTIVE_USER = "INACTIVE_USER";
    }
}
