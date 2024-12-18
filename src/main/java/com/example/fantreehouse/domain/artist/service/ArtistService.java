package com.example.fantreehouse.domain.artist.service;

import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.dto.request.ArtistRequestDto;
import com.example.fantreehouse.domain.artist.dto.response.ArtistProfileResponseDto;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
//import com.example.fantreehouse.domain.s3.service.S3FileUploader;
//import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
//import com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.common.enums.PageSize.ARTIST_PAGE_SIZE;
//import static com.example.fantreehouse.domain.s3.service.S3FileUploader.START_PROFILE_URL;
//import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.areFilesExist;
//import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.isFileExists;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;
//    private final S3FileUploader s3FileUploader;

    // 아티스트 계정 생성
    @Transactional
    public void createArtist(UserDetailsImpl userDetails, MultipartFile file, ArtistRequestDto requestDto) {
        User loginUser = userDetails.getUser();

        checkUserStatus(loginUser.getStatus());
        checkUserRole(loginUser.getUserRole());

        // 아티스트로 계정 등록이 되어 있는 유저인지 확인
        boolean isExist = artistRepository.existsByUserId(loginUser.getId());
        if (isExist) {
            throw new DuplicatedException(ENROLLED_USER_AS_ARTIST);
        }

        Artist newArtist = Artist.of(requestDto, loginUser);
        artistRepository.save(newArtist);

//        String imageUrl = "";
//        try {
//            imageUrl = s3FileUploader.saveProfileImage(file, newArtist.getId(), UserRoleEnum.ARTIST);
//        } catch (Exception e) {
//            s3FileUploader.deleteFileInBucket(imageUrl);
//            throw new S3Exception(UPLOAD_ERROR);
//        }
//
//        ImageUrlCarrier carrier = new ImageUrlCarrier(newArtist.getId(), imageUrl);
//        updateArtistImageUrl(carrier);
    }

    // 아티스트 프로필(계정) 수정
    @Transactional
    public void updateArtist(Long artistId, UserDetailsImpl userDetails, MultipartFile file, ArtistRequestDto requestDto) {
        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        checkUserRole(loginUser.getUserRole());

        Artist foundArtist = checkHimself(userDetails.getUser().getId(), artistId);
//        checkDuplicateName(requestDto.getArtistName(), foundArtist);
        foundArtist.updateArtist(requestDto);

//        if (isFileExists(file)) { // S3의 기존 이미지 삭제후 저장
//
//            try {
//                s3FileUploader.deleteFileInBucket(foundArtist.getArtistProfileImageUrl());
//            } catch (NotFoundException e) {
//                foundArtist.updateImageUrl("");//실체 없는 url 테이블에서 삭제
//                artistRepository.save(foundArtist);
//            } catch (Exception e) {
//                throw new S3Exception(DELETE_ERROR);
//            }
//
//            String newImageUrl = "";
//            try {
//                newImageUrl = s3FileUploader.saveProfileImage(file, foundArtist.getId(), UserRoleEnum.ARTIST);
//            } catch (Exception e) {
//                s3FileUploader.deleteFileInBucket(newImageUrl);
//                throw new S3Exception(UPLOAD_ERROR);
//            }
//
//            ImageUrlCarrier carrier = new ImageUrlCarrier(foundArtist.getId(), newImageUrl);
//            updateArtistImageUrl(carrier);
//        }
    }


    //아티스트 프로필(계정) 조회 - 비가입자 가능
    public ArtistProfileResponseDto getArtist(Long artistId) {

        Artist foundArtist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));

//        String url = s3FileUploader.getFileUrl(foundArtist.getArtistProfileImageUrl());

        return ArtistProfileResponseDto.of(foundArtist);
    }

    // 아티스트 프로필 전체 조회 - 비가입자 가능
    public Page<ArtistProfileResponseDto> getAllArtist(int page) {

        PageRequest pageRequest = PageRequest.of(page, ARTIST_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Artist> pageArtist = artistRepository.findAll(pageRequest);

        return pageArtist.map(ArtistProfileResponseDto::of);
    }

    // 아티스트 계정 삭제
    @Transactional
    public void deleteFeed(Long artistId, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        Artist foundArtist = checkHimself(userDetails.getUser().getId(), artistId);

//        try {
//            s3FileUploader.deleteFileInBucket(foundArtist.getArtistProfileImageUrl());
//        } catch (NotFoundException e) {
//            foundArtist.updateImageUrl("");
//            artistRepository.save(foundArtist);
//        } catch (Exception e) {
//            throw new S3Exception(DELETE_ERROR);
//        }
        artistRepository.delete(foundArtist);
    }

    // 아티스트가 속한 그룹 이름 조회
    public String getArtistGroupName(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        Artist artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_IN_GROUP));
        return artist.getArtistGroup().getGroupName();
    }


    // 활성화 유저인지 확인
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    //로그인한 유저가 수정/삭제하려는 아티스트와 동일한 아티스트인지
    private Artist checkHimself(Long userId, Long artistId) {
        Artist foundArtist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
        if (!foundArtist.getId().equals(artistId)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
        return foundArtist;
    }

//    // 활동명 중복 등록 확인
//    private void checkDuplicateName(String artistName, Artist artist) {
//
//        if (!artistName.equals(artist.getArtistName())) {
//            boolean isExistName = artistRepository.existsByArtistName(artistName);
//            if (isExistName) {
//                throw new DuplicatedException(ENROLLED_ARTIST_NAME);
//            }
//        }
//    }

    // 아티스트인지 확인
    private void checkUserRole(UserRoleEnum userRoleEnum) {
        if (!userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

//    //이미지 업데이트
//    private void updateArtistImageUrl(ImageUrlCarrier carrier) {
//        if (!carrier.getImageUrl().isEmpty()) {
//            Artist artist = artistRepository.findById(carrier.getId())
//                    .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
//            artist.updateImageUrl(carrier.getImageUrl());
//            artistRepository.save(artist);
//        }
//    }


//    //프로필 이미지 수정
//    @Transactional
//    public void updateProfileImage(MultipartFile file, Long userId) {
//        Artist artist = findById(userId);
//
//        String newImageUrl = controlS3Images(file, artist);
//        ImageUrlCarrier carrier = new ImageUrlCarrier(artist.getId(), newImageUrl);
//        updateArtistImageUrl(carrier);
//
//        artistRepository.save(artist);
//
//    }

//    private String controlS3Images(MultipartFile file, Artist artist) {
//
//        try {
//            s3FileUploader.deleteFileInBucket(artist.getArtistProfileImageUrl());
//        } catch (NotFoundException e) {
//            artist.updateImageUrl(START_PROFILE_URL);
//            artistRepository.save(artist);
//        } catch (Exception e) {
//            throw new S3Exception(DELETE_ERROR);
//        }
//
//        String newImageUrl;
//        try {
//            newImageUrl = s3FileUploader.saveProfileImage(file, artist.getId(), artist.getUser().getUserRole());
//        } catch (Exception e) {
//            s3FileUploader.deleteFileInBucket(artist.getArtistProfileImageUrl());
//            throw new S3Exception(UPLOAD_ERROR);
//        }
//        return newImageUrl;
//    }

    private Artist findById(Long userId) {
        return artistRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
    }
}