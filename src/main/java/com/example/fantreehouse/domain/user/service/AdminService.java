package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.user.dto.AdminRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public void transBlacklist(AdminRequestDto requestDto, User user) {
        // [예외1] - Admin 권한 체크
        checkAdminAuthority(user);

        // [예외 2] - 존재하지 않는 계정
        User blacklistUser = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        // 블랙리스트아닐 때
        if (! UserStatusEnum.BLACK_LIST.equals(blacklistUser.getStatus())) {
            blacklistUser.transBlacklist();
        } else {
            blacklistUser.transUser();
        }

        userRepository.save(blacklistUser);
    }

    @Transactional
    public void transRole(AdminRequestDto requestDto, User user) {
        // [예외1] - Admin 권한 체크
        checkAdminAuthority(user);

        // [예외 2] - 존재하지 않는 계정
        User transRoleUser = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(() ->
                new CustomException(ErrorType.USER_NOT_FOUND));

        transRoleUser.transRole(requestDto.getUserRole());

        userRepository.save(transRoleUser);
    }

    private void checkAdminAuthority(User user) {
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }
    }
}
