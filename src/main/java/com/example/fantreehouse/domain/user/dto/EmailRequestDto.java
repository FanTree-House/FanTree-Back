package com.example.fantreehouse.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class EmailRequestDto {

  private String loginId;

  @Email
  @NotEmpty(message = "이메일을 입력해 주세요")
  private String email;

}
