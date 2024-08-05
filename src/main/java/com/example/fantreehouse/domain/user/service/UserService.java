package com.example.fantreehouse.domain.user.service;

import com.example.fantreehouse.auth.RedisUtil;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.MismatchException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.isFileExists;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArtistGroupRepository artistGroupRepository;
    private final S3FileUploader s3FileUploader;

    @Value("${auth.admin_token}")
    private String ADMIN_TOKEN;
    @Value("${auth.artist_token}")
    private String ARTIST_TOKEN;
    @Value("${auth.entertainment_token}")
    private String ENTERTAINMENT_TOKEN;

    //회원가입
    public SignUpResponseDto signUp(MultipartFile file, SignUpRequestDto requestDto) {
        String id = requestDto.getId();
        String password = requestDto.getPassword();
        String checkPassowrd = passwordEncoder.encode(requestDto.getCheckPassword());
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String profile = requestDto.getProfileImageUrl();

        //ID 중복확인
        duplicatedId(id);

        //닉네임 중복확인
        duplicatedNickName(nickname);

        //비밀번호 재입력 및 확인
        checkPassword(password, checkPassowrd);

        String encodePassword = passwordEncoder.encode(password);
        // 블랙리스트 검증
        if (userRepository.findByEmailAndStatus(email, UserStatusEnum.BLACK_LIST).isPresent()) {
            throw new CustomException(ErrorType.BLACKLIST_EMAIL);
        }

        //이메일 검증 -> Null 검사
        if (redisUtil.getData(id) == null || !UserStatusEnum.ACTIVE_USER.equals(redisUtil.getData(id).getStatus())) {
            throw new CustomException(ErrorType.NOT_AUTH_EMAIL);
        }

        //redis에 저장된 이메일과 응답받은 이메일이 동일한지 체크
        if (!email.equals(redisUtil.getData(id).getEmail())) {
            throw new CustomException(ErrorType.NOT_AUTH_EMAIL);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new MismatchException(ErrorType.MISMATCH_ADMINTOKEN);
            }
            role = UserRoleEnum.ADMIN;
        } else if (requestDto.isArtist()) {
            if (!ARTIST_TOKEN.equals(requestDto.getArtistToken())) {
                throw new MismatchException(ErrorType.MISMATCH_ARTISTTOKEN);
            }
            role = UserRoleEnum.ARTIST;
        } else if (requestDto.isEntertainment()) {
            if (!ENTERTAINMENT_TOKEN.equals(requestDto.getEntertainmentToken())) {
                throw new MismatchException(ErrorType.MISMATCH_ENTERTAINMENTTOKEN);
            }
            role = UserRoleEnum.ENTERTAINMENT;
        }

        User user = new User(
                id,
                name,
                nickname,
                email,
                encodePassword,
                role
        );
        userRepository.save(user);

        String imageUrl = "";
        try {
            imageUrl = s3FileUploader.saveProfileImage(file, user.getId(), UserRoleEnum.USER);
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
        try {
            s3FileUploader.deleteFileInBucket(imageUrl);
        } catch (NotFoundException e) {
            user.updateImageUrl("");//실체 없는 url 테이블에서 삭제
            userRepository.save(user);
        } catch (Exception e) {
            throw new S3Exception(DELETE_ERROR);
        }
        user.withDraw();
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

        user.update(Optional.ofNullable(requestDto.getEmail()),
                Optional.ofNullable(newEncodePw));

        if (isFileExists(file)) { // S3의 기존 이미지 삭제후 저장

            try {
                s3FileUploader.deleteFileInBucket(user.getProfileImageUrl());
            } catch (NotFoundException e) {
                user.updateImageUrl("");
                userRepository.save(user);
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }

            String newImageUrl = "";
            try {
                newImageUrl = s3FileUploader.saveProfileImage(file, user.getId(), user.getUserRole());
            } catch (Exception e) {
                s3FileUploader.deleteFileInBucket(user.getProfileImageUrl());
                throw new S3Exception(UPLOAD_ERROR);
            }

            ImageUrlCarrier carrier = new ImageUrlCarrier(user.getId(), newImageUrl);
            updateUserImageUrl(carrier);
        }

        return new ProfileResponseDto(user);
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

    public void duplicatedId(String id) {
        if (userRepository.findByLoginId(id).isPresent()) {
            throw new DuplicatedException(ErrorType.DUPLICATE_ID);
        }
    }

    public void duplicatedNickName(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicatedException(ErrorType.DUPLICATE_NICKNAME);
        }
    }

    private void validBlackList(String email, UserStatusEnum status) {
        if (userRepository.findByEmailAndStatus(email, status).get().equals(UserStatusEnum.BLACK_LIST)) {
            throw new CustomException(ErrorType.BLACKLIST_EMAIL);
        }
    }

    //비밀번호 일치 확인
    private void checkPassword(String password, String checkPassword) {
        if (!passwordEncoder.matches(password, checkPassword)) {
            throw new MismatchException(ErrorType.MISMATCH_PASSWORD);
        }
    }


    private void updateUserImageUrl(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrl().isEmpty()) {
            User user = userRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
            user.updateImageUrl(carrier.getImageUrl());
            userRepository.save(user);
        }
    }
}