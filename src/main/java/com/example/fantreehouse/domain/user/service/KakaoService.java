package com.example.fantreehouse.domain.user.service;



import com.example.fantreehouse.auth.JwtTokenHelper;
import com.example.fantreehouse.domain.user.dto.KakaoUserInfoDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RestTemplate restTemplate;
  private final JwtTokenHelper jwtUtil;

  public String kakaoLogin(String code) throws JsonProcessingException {
    // 1. "인가 코드"로 "액세스 토큰" 요청
    String kakaoAccessToken = getToken(code);

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
    log.info("User API 요청 성공 : " + kakaoUserInfo.getEmail());

    // 3. 필요시에 회원가입
    User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

    // 4. JWT 토큰 반환
    String createKakaoToken = jwtUtil.createToken(kakaoUser.getName(),
       kakaoUser.getStatus(), kakaoUser.getUserRole());

    return createKakaoToken;
  }

  private String getToken(String code) throws JsonProcessingException {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
        .fromUriString("https://kauth.kakao.com")
        .path("/oauth/token")
        .encode()
        .build()
        .toUri();

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type",
        "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP Body 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", "2b0e2842adcacfeb9731a68eb4b42048");
    body.add("redirect_uri", "http://localhost:8080/user/kakao/callback");
    body.add("code", code);

    RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
        .post(uri)
        .headers(headers)
        .body(body);

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(
        requestEntity,
        String.class
    );

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
    return jsonNode.get("access_token").asText();
  }

  private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
    // 요청 URL 만들기
    URI uri = UriComponentsBuilder
        .fromUriString("https://kapi.kakao.com")
        .path("/v2/user/me")
        .encode()
        .build()
        .toUri();

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
        .post(uri)
        .headers(headers)
        .body(new LinkedMultiValueMap<>());

    // HTTP 요청 보내기
    ResponseEntity<String> response = restTemplate.exchange(
        requestEntity,
        String.class
    );

    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
    Long id = jsonNode.get("id").asLong();
    String nickname = jsonNode.get("properties")
        .get("nickname").asText();
    String email = jsonNode.get("kakao_account")
        .get("email").asText();

    log.debug("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
    return new KakaoUserInfoDto(id, nickname, email);
  }

  private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

    // DB 에 중복된 Kakao Id 가 있는지 확인
    Long kakaoId = kakaoUserInfo.getId();   // 카카오 DB 에서 가져온 User 'id'
    User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

    // 카카오로 로그인, 우리 사이트는 처음인 상태
    log.debug("kakaoUser 상태 : " + kakaoUser);
    if (kakaoUser == null) {
      // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
      String kakaoEmail = kakaoUserInfo.getEmail();
      log.debug("사용자 Email 확인");
      User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
      if (sameEmailUser != null) {
        log.debug("기존 사용자");
        // 이미 회원임, 근데 카카오 로그인만 처음
        kakaoUser = sameEmailUser;
        // 기존 회원정보에 카카오 Id 추가
        kakaoUser.kakaoIdUpdate(kakaoId);
      } else {
        log.debug("신규 사용자");
        // 우리 사이트 회원가입을 하려는 상태
        // 신규 회원가입
        // password: random UUID
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        kakaoUser = new User(
            kakaoUserInfo.getEmail(),// 계정 아이디
            kakaoUserInfo.getNickname(),
            kakaoUserInfo.getNickname(),    // 계정 성명
            kakaoUserInfo.getEmail(),              // 계정 이메일
            encodedPassword,    // 계정 비밀번호
            UserRoleEnum.USER,
            UserStatusEnum.ACTIVE_USER,
            kakaoUserInfo.getId()
        );
      }
      userRepository.save(kakaoUser);
    }
    log.debug("완료");
    return kakaoUser;
  }

}
