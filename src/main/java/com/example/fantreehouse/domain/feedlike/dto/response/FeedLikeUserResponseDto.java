package com.example.fantreehouse.domain.feedlike.dto.response;

import com.example.fantreehouse.domain.user.entity.User;

public class FeedLikeUserResponseDto {

    private String userNickName;
    private String profileImageUrl;

    public FeedLikeUserResponseDto(String userNickName, String profileImageUrl) {
        this.userNickName = userNickName;
        this.profileImageUrl = profileImageUrl;
    }

    public static FeedLikeUserResponseDto of(User user) {
        return new FeedLikeUserResponseDto(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
