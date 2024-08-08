package com.example.fantreehouse.common.dto;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.enums.ResponseStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseBooleanDto {

  private int status;
  private String message;
  private boolean result = false;

  public ResponseBooleanDto(ResponseStatus status, boolean result) {
    this.status = status.getHttpStatus().value();
    this.message = status.getMessage();
    this.result = result;
  }

  public ResponseBooleanDto(ErrorType errorType, boolean result) {
    this.status = errorType.getHttpStatus().value();
    this.message = errorType.getMessage();
    this.result = result;
  }

  public ResponseBooleanDto(ErrorType errorType){
    this.status = errorType.getHttpStatus().value();
    this.message = errorType.getMessage();
  }

}
