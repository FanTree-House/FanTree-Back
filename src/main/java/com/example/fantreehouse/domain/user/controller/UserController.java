package com.example.fantreehouse.domain.user.controller;


import com.example.fantreehouse.auth.JwtTokenHelper;
import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.user.dto.ProfileRequestDto;
import com.example.fantreehouse.domain.user.dto.ProfileResponseDto;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.dto.WithdrawRequestDto;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenHelper jwtTokenHelper;


    @PostMapping(value = {"/invite/entertainment", "/invite/artist", "/admin"})
    public ResponseEntity<ResponseMessageDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SIGNUP_SUCCESS));
    }

    @PutMapping("/withDraw")
    public ResponseEntity<ResponseMessageDto> withDraw(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @Valid @RequestBody WithdrawRequestDto requestDto) {
        userService.withDraw(userDetails.getUser().getId(), requestDto.getPassword());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.WITHDRAW_SUCCESS));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseMessageDto> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails.getUser().getId());
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

    @PutMapping
    public ResponseEntity<ResponseDataDto> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProfileRequestDto requestDto) {

        Long userId = userDetails.getUser().getId();
        ProfileResponseDto updateProfile = userService.updateProfile(userId, requestDto);

        return ResponseEntity.ok()
                .body(new ResponseDataDto(ResponseStatus.PROFILE_UPDATE, updateProfile));
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(
            @RequestHeader(JwtTokenHelper.AUTHORIZATION_HEADER) String accessToken,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info(accessToken);
        return ResponseEntity.ok()
                .body(userService.getProfile(userDetails.getUser().getId()));
    }
}
