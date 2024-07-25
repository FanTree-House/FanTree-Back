package com.example.fantreehouse.domain.artist.service;

import com.example.fantreehouse.common.exception.errorcode.AuthorizedException;
import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.dto.ArtistResponseDto;
import com.example.fantreehouse.domain.artist.dto.request.CreateArtistRequestDto;
import com.example.fantreehouse.domain.artist.dto.response.ArtistProfileResponseDto;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static java.lang.reflect.Array.set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional
    public void createArtist(UserDetailsImpl userDetails, CreateArtistRequestDto requestDto) {
        User loginUser = userDetails.getUser();

        checkUserStatus(loginUser.getStatus());
        checkUserRole(loginUser.getUserRole());

        // 아티스트로 계정 등록이 되어 있는 유저인지 확인
        boolean isExist = artistRepository.existsByUserId(loginUser.getId());
        if (isExist) {
            throw new DuplicatedException(ENROLLED_USER_AS_ARTIST);
        }

        // 활동명 중복 등록 확인
        boolean isExistName = artistRepository.existsByArtistName(requestDto.getArtistName());
        if(isExistName) {
            throw new DuplicatedException(ENROLLED_ARTIST_NAME);
        }

        // artist 등록
        Artist newArtist = Artist.of(requestDto, loginUser);
        artistRepository.save(newArtist);
    }

    // 아티스트 프로필 조회 - 가입한 유저
    public ArtistProfileResponseDto getArtist(Long artistId, UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());

        // 찾는 아티스트가 DB에 있는지 확인
        Artist foundArtist = artistRepository.findById(artistId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));

        return ArtistProfileResponseDto.of(foundArtist);
    }

    // 활성화 유저인지 확인
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }
    // 아티스트인지 확인
    private void checkUserRole(UserRoleEnum userRoleEnum) {
        if (!userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }
}