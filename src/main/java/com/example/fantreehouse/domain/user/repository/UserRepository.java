package com.example.fantreehouse.domain.user.repository;

import com.example.fantreehouse.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginId(String id);

  Optional <User> findByNickname(String nickname);

  Optional <User> findByEmail(String email);

  Optional<UserStatusEnum> getStatusFindByEmail(String email);
}
