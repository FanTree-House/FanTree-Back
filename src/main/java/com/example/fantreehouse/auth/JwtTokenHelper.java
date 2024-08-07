package com.example.fantreehouse.auth;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;

import com.nimbusds.jose.shaded.gson.annotations.Expose;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtTokenHelper")
@Component
@RequiredArgsConstructor
public class JwtTokenHelper {

  // Header KEY 값
  public static final String AUTHORIZATION_HEADER = "Authorization";
  // Refresh token
  public static final String REFRESH_TOKEN_HEADER = "Refresh_token";
  //
  public static  final String ACCESS_CONTROL_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";
  // 사용자 권한 값의 KEY
  public static final String AUTHORIZATION_KEY = "auth";
  // 회원 상태의 값 KEY
  public static final String STATUS_KET = "status";
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";
  // 토큰 만료시간
  private final long TOKEN_TIME = 30 * 60 * 1000L; // 30분

  private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L;
  // ADMIN USER

  private final UserRepository userRepository;

  @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // 토큰 생성
  public String createToken(String username, UserStatusEnum status, UserRoleEnum authority) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username) // 사용자 식별자값(ID)
            .claim(AUTHORIZATION_KEY, authority) // 사용자 권한
            .claim(STATUS_KET, status) // 회원 상태
            .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
            .setIssuedAt(date) // 발급일
            .signWith(key, signatureAlgorithm) // 암호화 알고리즘
            .compact();
  }

  public String createRefreshToken() {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setIssuedAt(date)
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  // header 에서 JWT 가져오기
  public String getJwtFromHeader(HttpServletRequest request, String tokenHeader) {
    String bearerToken = request.getHeader(tokenHeader);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  @Transactional
  public void saveRefreshToken(String id, String refreshToken) {
    User user = userRepository.findByLoginId(id)
        .orElseThrow(() -> new NotFoundException(ErrorType.USER_NOT_FOUND));
    user.saveRefreshToken(refreshToken);
  }

  public Claims getExpiredAccessToken(String accessToken) {
    try {
      return Jwts.parser().setSigningKey(secretKey)
          .parseClaimsJws(accessToken.substring(7)).getBody();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}