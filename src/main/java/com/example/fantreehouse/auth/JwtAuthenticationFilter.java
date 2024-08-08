package com.example.fantreehouse.auth;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.CommonErrorCode;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.user.dto.LoginRequestDto;
import com.example.fantreehouse.domain.user.dto.LoginResponseDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final JwtTokenHelper jwtTokenHelper;

  public JwtAuthenticationFilter(JwtTokenHelper jwtTokenHelper) {
    this.jwtTokenHelper = jwtTokenHelper;
    setFilterProcessesUrl("/users/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequestDto requestDto = new ObjectMapper().readValue(
          request.getInputStream(), LoginRequestDto.class);

      Authentication authentication = getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getId(),
              requestDto.getPassword(),
              null
          )
      );

      // 사용자 상태 체크
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      if (userDetails.getUser().getStatus().equals(UserStatusEnum.BLACK_LIST)) {
        throw new DisabledException("로그인이 불가능한 사용자입니다."); // 블랙리스트 예외 처리
      }
      return authentication;

    } catch (IOException e) {
      log.error(e.getMessage());
      throw new NotFoundException(CommonErrorCode.TOKEN_ERROR);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException {
    User user = ((UserDetailsImpl)authResult.getPrincipal()).getUser();
    String username = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();
    UserStatusEnum status = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getStatus();
    UserRoleEnum role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getUserRole();

    log.debug("username: {}", username);
    log.debug("status: {}", status);
    log.debug("role: {}", role);

    String accessToken = jwtTokenHelper.createToken(username, status, role);
    String refreshToken = jwtTokenHelper.createRefreshToken();
    response.addHeader(JwtTokenHelper.AUTHORIZATION_HEADER, accessToken);
    response.addHeader(JwtTokenHelper.REFRESH_TOKEN_HEADER, refreshToken);
    response.addHeader(JwtTokenHelper.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, "Authorization, Refresh_token" );
    jwtTokenHelper.loginDateAndSaveRefreshToken(username, refreshToken);

    // 로그인한 사용자의 userId와 userRole을 반환하도록 수정
    LoginResponseDto loginResponse = new LoginResponseDto(username, role);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new ObjectMapper()
            .writeValueAsString(new ResponseDataDto<>(ResponseStatus.LOGIN_SUCCESS, loginResponse)));
    response.getWriter().flush();
  }


  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    response.setCharacterEncoding("utf-8");
    if (failed instanceof DisabledException) {
      response.getWriter().write("상태 : " + response.getStatus() + ", " + failed.getMessage());
    } else {
      response.getWriter().write("상태 : " + response.getStatus() + ", 로그인 실패");
    }
  }
}