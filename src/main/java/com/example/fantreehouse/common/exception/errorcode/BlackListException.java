package com.example.fantreehouse.common.exception.errorcode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;

@Getter
public class BlackListException extends DisabledException {

  public BlackListException(String msg) {
    super(msg);
  }
}
