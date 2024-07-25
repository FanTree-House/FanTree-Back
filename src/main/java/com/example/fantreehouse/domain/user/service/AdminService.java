package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.user.dto.AdminRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final EnableSpringDataWebSupport.QuerydslActivator querydslActivator;

    @Transactional
    public void transBlacklist(AdminRequestDto requestDto, User user) {
        // [예외1] - Admin 권한 체크
        checkEntertainmentAuthority(user);

        // [예외 2] - 존재하지 않는 계정
        User blacklistUser = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ENTER));

        blacklistUser.transBlacklist();

        userRepository.save(blacklistUser);
    }

    private void checkEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }
    }
}
