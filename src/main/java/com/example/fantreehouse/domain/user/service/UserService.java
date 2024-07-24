package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpResponseDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

//    @Value("${auth.manager_token}")
    private String ADMIN_TOKEN;
    private String ENTERTAINMENT_TOKEN = "AAABnyxRVklrnYxKz0aHgTBcXukezYGoc";

    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();

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
            role = UserRoleEnum.ENTERTAINMENT;
        }

        User user = new User(
            id,
            name,
            nickname,
            email,
            password,
            role
        );
        userRepository.save(user);
        return new SignUpResponseDto(user);
    }

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

  private User findById(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
    );
  }

  @Transactional
  public boolean logout(Long id) {
    User user = findById(id);
    return user.logout();
  }

  public void refreshTokenCheck(String id, String refreshToken) {
    User user = userRepository.findByLoginId(id).orElseThrow(
        () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
    );

    if (!user.getRefreshToken().equals(refreshToken)) {
      throw new MismatchException(ErrorType.REFRESH_TOKEN_MISMATCH);
    }
  }
}