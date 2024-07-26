package com.example.fantreehouse.domain.user.controller;

import com.example.fantreehouse.auth.JwtTokenHelper;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.domain.user.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class SocialLoginController {

  private final KakaoService kakaoService;

  @GetMapping("/kakao/callback")
  public ResponseEntity<ResponseMessageDto> kakaoLogin(
      @RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
    String token = kakaoService.kakaoLogin(code);

    Cookie cookie = new Cookie(JwtTokenHelper.AUTHORIZATION_HEADER, token.substring(7));
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.LOGIN_SUCCESS));
  }
}
