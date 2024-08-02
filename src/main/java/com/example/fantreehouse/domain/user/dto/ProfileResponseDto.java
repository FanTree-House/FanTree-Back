package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
  private String loginId;
  private String name;
  private String email;
  private String nickname;
  private String profileImageUrl;

  public ProfileResponseDto(User user) {
    this.loginId = user.getLoginId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.profileImageUrl = user.getProfileImageUrl();
  }
}
