package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.auth.RedisUtil;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.user.dto.EmailCheckRequestDto;
import com.example.fantreehouse.domain.user.dto.EmailRequestDto;
import com.example.fantreehouse.domain.user.dto.ProfileRequestDto;
import com.example.fantreehouse.domain.user.dto.ProfileResponseDto;
import com.example.fantreehouse.domain.user.dto.SignUpRequestDto;
import com.example.fantreehouse.domain.user.dto.SignUpResponseDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.service.S3FileUploader.BASIC_DIR;
import static com.example.fantreehouse.domain.s3.service.S3FileUploader.START_PROFILE_URL;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3FileUploader s3FileUploader;
    private final MailSendService mailSendService;

    //회원가입
    public SignUpResponseDto signUp(MultipartFile file, SignUpRequestDto requestDto) {
        //비밀번호를 엔티티에서 꺼내올 필요가 있을까?
        String id = requestDto.getId();
        String password = requestDto.getPassword();
        String checkPassowrd = requestDto.getCheckPassword();
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        MultipartFile profile = requestDto.getFile();

        //ID 중복 확인
        if (duplicatedId(id)){
            throw new DuplicatedException(ErrorType.DUPLICATE_ID);
        }
        // 닉네임 중복 확인
        if (duplicatedNickName(nickname)){
            throw new DuplicatedException(ErrorType.DUPLICATE_NICKNAME);
        }
        //비밀번호 재입력 및 확인
        if (!checkPassword(password, checkPassowrd)){
            throw new MismatchException(ErrorType.MISMATCH_PASSWORD);
        }

        String encodePassword = passwordEncoder.encode(password);

        // 블랙리스트 검증
        if (userRepository.findByEmailAndStatus(email, UserStatusEnum.BLACK_LIST).isPresent()) {
            throw new CustomException(ErrorType.BLACKLIST_EMAIL);
        }

       verifyEmail(id,email);

        User user = new User(
                id,
                name,
                nickname,
                email,
                encodePassword,
                UserRoleEnum.valueOf(requestDto.getUserRoleEnum())
        );
        userRepository.save(user);

        //메서드로 분리
        String imageUrl = START_PROFILE_URL;
        try {
            imageUrl = s3FileUploader.saveProfileImage(file, user.getId(), user.getUserRole());
        } catch (Exception e) {
            s3FileUploader.deleteFileInBucket(imageUrl);
            throw new S3Exception(UPLOAD_ERROR);
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(user.getId(), imageUrl);
        updateUserImageUrl(carrier);

        redisUtil.deleteData(id);
        return new SignUpResponseDto(user);
    }

    //회원 탈퇴
    @Transactional
    public void withDraw(Long userId, String password) {
        User user = findById(userId);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new MismatchException(ErrorType.MISMATCH_PASSWORD);
        }
        if (user.getStatus().equals(UserStatusEnum.WITHDRAW_USER)) {
            throw new NotFoundException(ErrorType.WITHDRAW_USER);
        }

        String imageUrl = user.getProfileImageUrl();
        if (!imageUrl.startsWith(BASIC_DIR)) {
            try {
                s3FileUploader.deleteFileInBucket(imageUrl);
            } catch (NotFoundException e) {
                user.updateImageUrl(START_PROFILE_URL);//실체 없는 url 테이블에서 삭제
                userRepository.save(user);
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }
        }
        user.withDraw();
        userRepository.save(user);
    }

    // 로그아웃
    @Transactional
    public boolean logout(Long id) {
        User user = findById(id);
        return user.logout();
    }

    //refreshToken 확인
    public void refreshTokenCheck(String id, String refreshToken) {
        User user = userRepository.findByLoginId(id).orElseThrow(
                () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
        );

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new MismatchException(ErrorType.REFRESH_TOKEN_MISMATCH);
        }
    }

    //유저 프로필 수정
    @Transactional
    public ProfileResponseDto updateProfile(MultipartFile file, Long userId, ProfileRequestDto requestDto) {
        User user = findById(userId);
        String newEncodePw = null;

        if (requestDto.getPassword() != null) {
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                newEncodePw = passwordEncoder.encode(requestDto.getNewPassword());
            }
        }

        if (requestDto.getNickname() != null) {
            if (duplicatedNickName(requestDto.getNickname())){
                throw new DuplicatedException(ErrorType.DUPLICATE_NICKNAME);
            }
        }

        user.update(Optional.ofNullable(requestDto.getEmail()),
                Optional.ofNullable(newEncodePw),
                Optional.ofNullable(requestDto.getNickname())
                );

        if (isFileExists(file)) { // S3의 기존 이미지 삭제후 저장

            String newImageUrl = controlS3Images(file, user);
            ImageUrlCarrier carrier = new ImageUrlCarrier(user.getId(), newImageUrl);
            updateUserImageUrl(carrier);
        }
        userRepository.save(user);

        return new ProfileResponseDto(user);
    }

    //프로필 이미지 수정
    @Transactional
    public void updateProfileImage(MultipartFile file, Long userId) {
        User user = findById(userId);

        String newImageUrl = controlS3Images(file, user);
        ImageUrlCarrier carrier = new ImageUrlCarrier(user.getId(), newImageUrl);
        updateUserImageUrl(carrier);

        userRepository.save(user);
    }

    //유저 프로필 조회
    public ProfileResponseDto getProfile(Long userId) {
        return new ProfileResponseDto(findById(userId));
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorType.USER_NOT_FOUND)
        );
    }

    //Id 중복 확인
    public boolean duplicatedId(String id) {
        return userRepository.existsByLoginId(id);
    }

    //닉네임 중복 확인
    public boolean duplicatedNickName(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    private void updateUserImageUrl(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrl().isEmpty()) {
            User user = userRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
            user.updateImageUrl(carrier.getImageUrl());
            userRepository.save(user);
        }
    }

    public boolean existsInactiveUser(EmailRequestDto requestDto){
        return userRepository.existsByLoginIdAndEmailAndStatus(requestDto.getLoginId(),
            requestDto.getEmail(),UserStatusEnum.INACTIVE_USER);
    }

    @Transactional
    public void activateUser(EmailCheckRequestDto requestDto) {
        User user = userRepository.findByLoginIdAndEmailAndStatus(requestDto.getLoginId(),
            requestDto.getEmail(), UserStatusEnum.INACTIVE_USER).orElseThrow(()
                -> new NotFoundException(USER_NOT_FOUND));
        mailSendService.CheckAuthNum(requestDto.getLoginId(),requestDto);
        verifyEmail(requestDto.getLoginId(), requestDto.getEmail());

        User findUser = userRepository.findById(user.getId()).orElseThrow(()
            -> new NotFoundException(USER_NOT_FOUND));
       findUser.activateUser();
        userRepository.save(findUser);
        redisUtil.deleteData(requestDto.getLoginId());
    }

    private void verifyEmail(String id, String email){
        //이메일 검증 -> Null 검사
        if (redisUtil.getData(id) == null || !UserStatusEnum.ACTIVE_USER.
            equals(redisUtil.getData(id).getStatus())){
            throw new CustomException(ErrorType.NOT_AUTH_EMAIL);
        }
        //redis에 저장된 이메일과 응답받은 이메일이 동일한지 체크
        if (!email.equals(redisUtil.getData(id).getEmail())) {
            throw new CustomException(ErrorType.NOT_AUTH_EMAIL);
        }
    }

    public boolean checkPassword (String password, String checkPassword) {
        return password.equals(checkPassword);
    }

    //S3에 프로필 이미지 삭제 및 저장
    private String controlS3Images(MultipartFile file, User user) {
        try {
            s3FileUploader.deleteFileInBucket(user.getProfileImageUrl());
        } catch (NotFoundException e) {
            user.updateImageUrl(START_PROFILE_URL);
            userRepository.save(user);
        } catch (Exception e) {
            throw new S3Exception(DELETE_ERROR);
        }

        String newImageUrl;
        try {
            newImageUrl = s3FileUploader.saveProfileImage(file, user.getId(), user.getUserRole());
        } catch (Exception e) {
            s3FileUploader.deleteFileInBucket(user.getProfileImageUrl());
            throw new S3Exception(UPLOAD_ERROR);
        }
        return newImageUrl;
    }

}