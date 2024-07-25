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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntertainmentService {

    private final EntertainmentRepository enterRepository;

    /**
     * 엔터 계정 생성
     * @param enterRequestDto
     * @param user
     */
    @Transactional
    public void createEnter(EntertainmentRequestDto enterRequestDto, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        Entertainment enter = new Entertainment(enterRequestDto, user);

        // [예외2] - Entertainment 소속사 이름, 사업자번호 중복체크
        checkEnterNameOrNumberExisits(enter);

        enterRepository.save(enter);
    }

    /**
     * 엔터 계정 조회
     * @param enterName
     * @param user
     * @return
     */
    public EntertainmentResponseDto getEnter(String enterName, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외 2] - 존재하지 않는 엔터테이먼트 계정
        Entertainment enter = enterRepository.findByEnterName(enterName).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_ENTER)
        );

        EntertainmentResponseDto enterResponseDto = new EntertainmentResponseDto(enter);

        return enterResponseDto;
    }

    /**
     * 엔터 계정 수정
     * @param enterName
     * @param enterRequestDto
     * @param user
     */
    @Transactional
    public void updateEnter(String enterName, EntertainmentRequestDto enterRequestDto, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외 2] - 존재하지 않는 엔터테이먼트 계정
        Entertainment enter = enterRepository.findByEnterName(enterName).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ENTER));

        if (null != enterRequestDto.getEnterName() && null == enterRequestDto.getEnterNumber() && null == enterRequestDto.getEnterLogo()) {
            enter.updateEnterName(enterRequestDto.getEnterName());
        } else if (null == enterRequestDto.getEnterName() && null != enterRequestDto.getEnterNumber() && null == enterRequestDto.getEnterLogo()) {
            enter.updateEnterNumber(enterRequestDto.getEnterNumber());
        } else if (null == enterRequestDto.getEnterName() && null == enterRequestDto.getEnterNumber() && null != enterRequestDto.getEnterLogo()) {
            enter.updateEnterLogo(enterRequestDto.getEnterLogo());
        }

        enterRepository.save(enter);
    }

    /**
     * 엔터 계정 삭제
     * @param enterName
     * @param user
     */
    @Transactional
    public void deleteEnter(String enterName, User user) {
        // [예외1] - Entertainment, Admin 권한 체크
        if (!(UserRoleEnum.ADMIN.equals(user.getUserRole()) || UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole()))) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }

        // [예외 2] - 존재하지 않는 엔터테이먼트 계정
        Entertainment enter = enterRepository.findByEnterName(enterName).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ENTER));

        enterRepository.delete(enter);
    }

    // Entertainment 권한 체크
    private void checkEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }
    }

    // 소속사 이름, 사업자번호 중복체크
    private void checkEnterNameOrNumberExisits(Entertainment enter) {
        if (enterRepository.findByEnterName(enter.getEnterName()).isPresent()) {
            throw new CustomException(ErrorType.ALREADY_EXIST_ENTER_NAME);
        } else if (enterRepository.findByEnterNumber(enter.getEnterNumber()).isPresent()) {
            throw new CustomException(ErrorType.ALREADY_EXIST_ENTER_NUMBER);
        }
    }
}
