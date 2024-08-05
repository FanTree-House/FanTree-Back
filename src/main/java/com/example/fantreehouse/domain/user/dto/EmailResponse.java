package com.example.fantreehouse.domain.user.dto;

import com.example.fantreehouse.domain.user.entity.MailAuth;
import lombok.Getter;

@Getter
public class EmailResponse {
  private String loginId;

  private String email;

  public EmailResponse(MailAuth mailAuth) {
    this.loginId = mailAuth.getLoginId();
    this.email = mailAuth.getEmail();
  }
}
