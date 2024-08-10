package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.common.dto.ResponseErrorMessageDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.domain.user.dto.EmailCheckRequestDto;
import com.example.fantreehouse.domain.user.dto.EmailRequestDto;

import com.example.fantreehouse.domain.user.service.MailSendService;
import com.example.fantreehouse.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InactiveUserController {

  private final MailSendService mailSendService;
  private final UserService userService;

  @PostMapping("/Inactive")
  public ResponseEntity<?> fromInactiveToActive(EmailRequestDto requestDto){
    if (userService.existsInactiveUser(requestDto)){
      mailSendService.changeInactiveUserStatusEmail(requestDto);
      return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CHECK_EMAIL));
    }
    return ResponseEntity.ok(new ResponseErrorMessageDto(ErrorType.CHECK_YOUR_INFO));
  }

  public ResponseEntity<ResponseMessageDto> activateUser(EmailCheckRequestDto requestDto){
    userService.activateUser(requestDto);
    return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS_ACTIVATE_USER));
  }


}
