package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.User;
import lombok.Getter;

@Getter
public class SignUpResponseDto {

  private String id;
  private String name;
  private String nickname;
  private String email;
  private String password;

  public SignUpResponseDto(User user) {
    this.id = user.getLoginId();
    this.name = user.getName();
    this.password = user.getPassword();
    this.nickname = user.getNickname();
    this.email = user.getEmail();
  }
}
