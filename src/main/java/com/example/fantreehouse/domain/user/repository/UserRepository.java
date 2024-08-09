package com.example.fantreehouse.domain.user.repository;

import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginId(String id);

  Optional<User> findByEmail(String email);

  Optional<User> findByKakaoId(Long kakaoId);

  Optional<User> findByEmailAndStatus(String email, UserStatusEnum status);

  boolean existsByLoginId(String id);

  boolean existsByNickname(String nickname);

  boolean existsByLoginIdAndEmailAndStatus(String loginId, String email, UserStatusEnum status);

  Optional<User> findByLoginIdAndEmailAndStatus(String loginId, String email, UserStatusEnum status);

}
