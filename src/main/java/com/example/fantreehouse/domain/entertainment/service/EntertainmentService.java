package com.example.fantreehouse.domain.entertainment.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntertainmentService {
    private final EntertainmentRepository enterRepository;

    public void createEnter(EntertainmentRequestDto enterRequestDto, User user) {
        // [예외1] - Entertainment 권한이 아닌 USER가 생성을 시도할 경우
        //checkEntertainmentAuthority(loginUser);
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_FOUND_ENTER);
        }

        Entertainment enter = new Entertainment(enterRequestDto, user);
        // [예외2] - Entertainment 소속사 이름, 사업자번호 중복체크
        //checkEnterNameOrNumberExisits(enter);

        enterRepository.save(enter);
    }


    public EntertainmentResponseDto getEnter(String enterName) {

        Entertainment enter = enterRepository.findByEnterName(enterName);
        // [예외 1] - 존재하지 않는 엔터테이먼트 계정
        // orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ENTER));

        return null;
    }
}
