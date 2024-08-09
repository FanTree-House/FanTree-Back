package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.dto.ResponseBooleanDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.enums.ResponseStatus;
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
  public ResponseEntity<ResponseMessageDto> mailSend(@RequestBody @Valid EmailRequestDto requestDto) {
    mailSendService.joinEmail(requestDto);
    return ResponseEntity.ok().body(new ResponseMessageDto(ResponseStatus.CHECK_EMAIL));
  }

  @PostMapping("/mailableCheck")
  public ResponseEntity<ResponseBooleanDto> AuthCheck(@RequestBody @Valid EmailCheckRequestDto requestDto) {

    String loginId = requestDto.getLoginId();
    if (null == requestDto.getAuthNum()) {
      return ResponseEntity.ok().body(new ResponseBooleanDto(ErrorType.AUTH_NUM_NOTFOUND));
    }
    boolean result = mailSendService.CheckAuthNum(loginId, requestDto);
    if (result){
      return ResponseEntity.ok(new ResponseBooleanDto(ResponseStatus.CHECK_AUTHNUM, result));
    }
    else return ResponseEntity.ok(new ResponseBooleanDto(ErrorType.AUTH_MISMATCH, result));
  }
}
