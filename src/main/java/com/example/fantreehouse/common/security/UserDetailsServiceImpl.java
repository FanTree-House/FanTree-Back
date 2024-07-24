package com.example.fantreehouse.common.security;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    User user = userRepository.findByLoginId(id)
        .orElseThrow(() -> new NotFoundException(ErrorType.USER_NOT_FOUND));
    if (user.getStatus() == UserStatusEnum.WITHDRAW_USER) {
      throw new MismatchException(ErrorType.WITHDRAW_USER);
    }

    return new UserDetailsImpl(user);
  }
}
