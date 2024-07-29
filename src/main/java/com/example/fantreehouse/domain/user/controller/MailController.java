package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.domain.user.dto.EmailCheckRequestDto;
import com.example.fantreehouse.domain.user.dto.EmailRequestDto;
import com.example.fantreehouse.domain.user.entity.MailAuth;
import com.example.fantreehouse.domain.user.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

  private final MailSendService mailSendService;

  @PostMapping("/mailsend")
  public String mailSend(@RequestBody @Valid EmailRequestDto requestDto, MailAuth mailAuth) {
    return mailSendService.joinEmail(requestDto.getEmail(), mailAuth);
  }

  @PutMapping("/mailableCheck")
  public ResponseEntity AuthCheck(@RequestBody @Valid EmailCheckRequestDto requestDto) {

    String email = requestDto.getEmail();
    if (requestDto.getAuthNum() == null) {
      return ResponseEntity.status(200).body(ErrorType.AUTH_NUM_NOTFOUND);
    }
    mailSendService.CheckAuthNum(email, requestDto);
    return ResponseEntity.ok().body("인증 완료 했습니다");
  }
}
