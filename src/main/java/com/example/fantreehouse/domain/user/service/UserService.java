package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.user.dto.ProfileResponseDto;
import com.example.fantreehouse.domain.user.dto.ProfileRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpResponseDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;


  //    @Value("${auth.manager_token}")
  private String ADMIN_TOKEN = "1np0t2ncesuuuud3rTaMeng5" ;

    private String ARTIST_TOKEN = "acRos3knitterUp2eTt1ng5";

    private String ENTERTAINMENT_TOKEN = "AAABnyxRVklrnYxKz0aHgTBcXukezYGoc";

    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String profile = requestDto.getProfileImage();

        //ID 검증
        if (userRepository.findByLoginId(id).isPresent()){
            throw new DuplicatedException(ErrorType.DUPLICATE_ID);
        }

        //닉네임 검증
        if (userRepository.findByNickname(nickname).isPresent()){
            throw new DuplicatedException(ErrorType.DUPLICATE_NICKNAME);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isEntertainment()){
            if(!ENTERTAINMENT_TOKEN.equals(requestDto.getEntertainmentToken())){
                throw new MismatchException(ErrorType.MISMATCH_ENTERTAINMENTTOKEN);
            }
            role = UserRoleEnum.ADMIN;
        }

        else if(requestDto.isArtist()){
            if(!ARTIST_TOKEN.equals(requestDto.getArtistToken())){
                throw new MismatchException(ErrorType.MISMATCH_ARTISTTOKEN);
            }
            role = UserRoleEnum.ARTIST;
        }

        else if(requestDto.isEntertainment()){
            if(!ENTERTAINMENT_TOKEN.equals(requestDto.getEntertainmentToken())){
              throw new MismatchException(ErrorType.MISMATCH_ENTERTAINMENTTOKEN);
            }
            role = UserRoleEnum.ENTERTAINMENT;
        }

        User user = new User(
            id,
            name,
            nickname,
            email,
            password,
            profile,
            role
        );
        userRepository.save(user);
        return new SignUpResponseDto(user);
    }


  //회원 탈퇴
  @Transactional
  public void withDraw(Long userId, String password) {
    User user = findById(userId);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new MismatchException(ErrorType.MISMATCH_PASSWORD);
    }
    if (user.getStatus().equals(UserStatusEnum.WITHDRAW_USER)) {
      throw new NotFoundException(ErrorType.WITHDRAW_USER);
    }
    user.withDraw();
  }

  // 로그아웃
  @Transactional
  public boolean logout(Long id) {
    User user = findById(id);
    return user.logout();
  }

  //refreshToken 확인
  public void refreshTokenCheck(String id, String refreshToken) {
    User user = userRepository.findByLoginId(id).orElseThrow(
        () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
    );

    if (!user.getRefreshToken().equals(refreshToken)) {
      throw new MismatchException(ErrorType.REFRESH_TOKEN_MISMATCH);
    }
  }

  //유저 프로필 수정
  @Transactional
  public ProfileResponseDto updateProfile(Long userId, ProfileRequestDto requestDto) {
    User user = findById(userId);
    String newEncodePw = null;

    if (requestDto.getPassword() != null) {
      if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
        newEncodePw = passwordEncoder.encode(requestDto.getNewPassword());
      }
    }

    user.update(Optional.ofNullable(requestDto.getEmail()),
        Optional.ofNullable(newEncodePw));
    return new ProfileResponseDto(user);
    }

  //유저 프로필 조회
  public ProfileResponseDto getProfile(Long userId) {
    return new ProfileResponseDto(findById(userId));
  }


  private User findById(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
    );
  }
}