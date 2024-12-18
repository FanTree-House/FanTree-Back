package com.example.fantreehouse.domain.entertainment.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
//import com.example.fantreehouse.domain.s3.service.S3FileUploader;
//import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
//import com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.fantreehouse.common.enums.ErrorType.*;
//import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.isFileExists;

@Service
@RequiredArgsConstructor

public class EntertainmentService {

    private final EntertainmentRepository enterRepository;
    private final UserRepository userRepository;
//    private final S3FileUploader s3FileUploader;

    /**
     * 엔터 계정 생성
     *
     * @param enterRequestDto
     * @param userId
     */
    @Transactional
    public void createEnter(MultipartFile file, EntertainmentRequestDto enterRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        Entertainment enter = new Entertainment(
                enterRequestDto.getEnterName(),
                enterRequestDto.getEnterNumber(),
                user);
        // [예외2] - Entertainment 소속사 이름, 사업자번호 중복체크
        checkEnterNameOrNumberExisits(enter);

        enterRepository.save(enter);

//        String imageUrl = "";
//        try {
//            imageUrl = s3FileUploader.saveProfileImage(file, enter.getId(), UserRoleEnum.ENTERTAINMENT);
//        } catch (Exception e) {
//            s3FileUploader.deleteFileInBucket(imageUrl);
//            throw new S3Exception(UPLOAD_ERROR);
//        }
//
//        ImageUrlCarrier carrier = new ImageUrlCarrier(enter.getId(), imageUrl);
//        updateEnterLogoUrl(carrier);
    }


    /**
     * 로그인된 사용자가 소유한 엔터 계정 조회
     *
     * @param user 로그인된 사용자
     * @return 엔터 계정 정보
     */
    public EntertainmentResponseDto getEnter(User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외 2] - 사용자가 소유한 엔터테이먼트 계정이 없는 경우 예외 발생
        Entertainment entertainment = enterRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_ENTER));

        // 조회된 엔터테이먼트 계정 정보를 DTO로 변환하여 반환
        return new EntertainmentResponseDto(entertainment);
    }


    /**
     * 엔터 계정 수정
     *
     * @param enterName
     * @param enterRequestDto
     * @param user
     */
    @Transactional
    public void updateEnter(MultipartFile file, String enterName, EntertainmentRequestDto enterRequestDto, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외 2] - 존재하지 않는 엔터테이먼트 계정
        Entertainment enter = enterRepository.findByEnterName(enterName).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_ENTER));

        if (null != enterRequestDto.getEnterName()) {
            enter.updateEnterName(enterRequestDto.getEnterName());
        } else if (null != enterRequestDto.getEnterNumber()) {
            enter.updateEnterNumber(enterRequestDto.getEnterNumber());
        }

        enterRepository.save(enter);

//        if (isFileExists(file)) { // S3의 기존 이미지 삭제후 저장
//            try {
//                s3FileUploader.deleteFileInBucket(enter.getEnterLogo());
//            } catch (NotFoundException e) {
//                enter.updateEnterLogo("");
//                enterRepository.save(enter);
//            } catch (Exception e) {
//                throw new S3Exception(DELETE_ERROR);
//            }
//
//            String newImageUrl = "";
//            try {
//                newImageUrl = s3FileUploader.saveProfileImage(file, enter.getId(), UserRoleEnum.ENTERTAINMENT);
//            } catch (Exception e) {
//                s3FileUploader.deleteFileInBucket(newImageUrl);
//                throw new S3Exception(UPLOAD_ERROR);
//            }
//
//            ImageUrlCarrier carrier = new ImageUrlCarrier(enter.getId(), newImageUrl);
//            updateEnterLogoUrl(carrier);
//        }
    }


    /**
     * 엔터 계정 삭제
     *
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

//        try {
//            s3FileUploader.deleteFileInBucket(enter.getEnterLogo());
//        } catch (NotFoundException e) {
//            enter.updateEnterLogo("");//실체 없는 url 테이블에서 삭제
//            enterRepository.save(enter);
//        } catch (Exception e) {
//            throw new S3Exception(DELETE_ERROR);
//        }
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

//    private void updateEnterLogoUrl(ImageUrlCarrier carrier) {
//        if (!carrier.getImageUrl().isEmpty()) {
//            Entertainment entertainment = enterRepository.findById(carrier.getId())
//                    .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
//            entertainment.updateEnterLogo(carrier.getImageUrl());
//            enterRepository.save(entertainment);
//        }
//    }
}