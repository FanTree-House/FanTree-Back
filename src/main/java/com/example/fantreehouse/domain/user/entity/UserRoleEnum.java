package com.example.fantreehouse.domain.user.entity;

public enum UserRoleEnum {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER),
    ARTIST(Authority.ARTIST),
    ENTERTAINMENT(Authority.ENTERTAINMENT);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {

        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
        public static final String ARTIST = "ARTIST";
        public static final String ENTERTAINMENT = "ENTERTAINMENT";

    }
}
