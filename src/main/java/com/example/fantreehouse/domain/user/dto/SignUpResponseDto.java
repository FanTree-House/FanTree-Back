package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class SignUpResponseDto {

  private String id;
  private String name;
  private String nickname;
  private String email;
  private UserRoleEnum userRoleEnum;

  public SignUpResponseDto(User user) {
    this.id = user.getLoginId();
    this.name = user.getName();
    this.nickname = user.getNickname();
    this.email = user.getEmail();
    this.userRoleEnum = user.getUserRole();
  }
}
