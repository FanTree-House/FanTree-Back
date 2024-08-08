package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.Getter;

@Getter
public class ForbiddenResponseDto {
  private UserStatusEnum statusEnum;
  private int status;
  private String message;

  public ForbiddenResponseDto(UserStatusEnum statusEnum, ErrorType errorType) {
    this.statusEnum = statusEnum;
    this.status = errorType.getHttpStatus().value();
    this.message = errorType.getMessage();
  }



}
