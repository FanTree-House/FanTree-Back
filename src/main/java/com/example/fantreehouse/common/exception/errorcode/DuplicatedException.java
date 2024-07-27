package com.example.fantreehouse.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicatedException extends RuntimeException {

  private final ErrorCode errorCode;
}
