package com.example.fantreehouse.common.security;

import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

  private final User user;

  public UserDetailsImpl(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getLoginId();
  }

  //사용자 권한 정하는 메서드
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserStatusEnum statusEnum = user.getStatus();
    String status = statusEnum.getStatus();

    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(status);

    Collection<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(simpleGrantedAuthority);

    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}