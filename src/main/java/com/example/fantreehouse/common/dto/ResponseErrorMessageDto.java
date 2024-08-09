package com.example.fantreehouse.common.dto;

import com.example.fantreehouse.common.enums.ErrorType;
import lombok.Getter;

@Getter
public class ResponseErrorMessageDto {
  private int status;
  private String message;

  public ResponseErrorMessageDto(ErrorType errorType){
    this.status = errorType.getHttpStatus().value();
    this.message = errorType.getMessage();
  }
}
