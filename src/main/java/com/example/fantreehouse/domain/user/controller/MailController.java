package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.domain.user.dto.EmailCheckRequestDto;
import com.example.fantreehouse.domain.user.dto.EmailRequestDto;
import com.example.fantreehouse.domain.user.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

  private final MailSendService mailSendService;

  @PostMapping("/mailsend")
  public ResponseEntity mailSend(@RequestBody @Valid EmailRequestDto requestDto) {
    mailSendService.joinEmail(requestDto);
    return ResponseEntity.ok().body("메일을 확인해주세요.");
  }

  @PostMapping("/mailableCheck")
  public ResponseEntity AuthCheck(@RequestBody @Valid EmailCheckRequestDto requestDto) {

    String loginId = requestDto.getLoginId();
    if (null == requestDto.getAuthNum()) {
      return ResponseEntity.status(200).body(ErrorType.AUTH_NUM_NOTFOUND);
    }
    mailSendService.CheckAuthNum(loginId, requestDto);
    return ResponseEntity.ok().body("인증 완료 했습니다");
  }
}
