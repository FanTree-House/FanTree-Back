package com.example.fantreehouse.common.exception.errorcode;

import lombok.Getter;
import org.springframework.security.authentication.DisabledException;

@Getter
public class InactiveException extends DisabledException {

  public InactiveException(String msg) {
    super(msg);
  }
}
