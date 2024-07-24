package com.example.fantreehouse.domain.user.controller;


import com.example.fantreehouse.auth.JwtTokenHelper;
import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.domain.user.dto.LoginRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.service.UserService;
import com.example.fantreehouse.common.enums.ResponseStatus;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final JwtTokenHelper jwtTokenHelper;


  @PostMapping
    public ResponseEntity<ResponseMessageDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SIGNUP_SUCCESS));
    }

    @PutMapping("/withDraw/{id}")
    public ResponseEntity<ResponseMessageDto> withDraw(@PathVariable Long id, String password) {
        userService.withDraw(id, password);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.WITHDRAW_SUCCESS));
    }

    @PutMapping("/logout/{id}")
    public ResponseEntity<ResponseMessageDto> logout(@PathVariable Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.LOGOUT_SUCCESS));
    }
  @GetMapping("/refresh")
  public ResponseEntity<ResponseDataDto> refresh(
      @RequestHeader(JwtTokenHelper.AUTHORIZATION_HEADER) String accessToken,
      @RequestHeader(JwtTokenHelper.REFRESH_TOKEN_HEADER) String refreshToken) {

    Claims claims = jwtTokenHelper.getExpiredAccessToken(accessToken);
    String username = claims.getSubject();
    String status = claims.get("status").toString();
    String role = claims.get("auth").toString();

    UserStatusEnum statusEnum = UserStatusEnum.valueOf(status);
    UserRoleEnum roleEnum = UserRoleEnum.valueOf(role);

    userService.refreshTokenCheck(username, refreshToken);

    String newAccessToken = jwtTokenHelper.createToken(username, statusEnum, roleEnum);
    return ResponseEntity.ok()
        .header(JwtTokenHelper.AUTHORIZATION_HEADER, newAccessToken)
        .body(new ResponseDataDto(ResponseStatus.UPDATE_TOKEN_SUCCESS_MESSAGE, newAccessToken));
  }

}
