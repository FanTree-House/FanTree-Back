package com.example.fantreehouse.domain.entertainment.service;

import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntertainmentService {
    private final EntertainmentRepository enterRepository;

    public void createEnter(EntertainmentRequestDto enterRequestDto/*, User user*/) {
        // [예외1] - Entertainment 권한이 아닌 USER가 생성을 시도할 경우
        //checkEntertainmentAuthority(loginUser);

        Entertainment enter = new Entertainment(enterRequestDto/*, user*/);
        // [예외2] - Entertainment 소속사 이름, 사업자번호 중복체크
        //checkEnterNameOrNumberExisits(enter);

        enterRepository.save(enter);
    }



}
