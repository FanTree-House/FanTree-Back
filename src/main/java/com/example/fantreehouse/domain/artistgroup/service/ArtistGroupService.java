package com.example.fantreehouse.domain.artistgroup.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.artist.dto.ArtistResponseDto;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupRequestDto;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupResponseDto;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.service.S3FileUploader.BASIC_DIR;
import static com.example.fantreehouse.domain.s3.service.S3FileUploader.START_PROFILE_URL;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.isFileExists;

@Service
@RequiredArgsConstructor
public class ArtistGroupService {

    private final ArtistGroupRepository artistGroupRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final ArtistRepository artistRepository;
    private final S3FileUploader s3FileUploader;

    /**
     * 아티스트 그룹 생성
     *
     * @param request 요청 객체
     * @param user    로그인한 사용자 정보
     * @return 생성된 아티스트 그룹
     */
    @Transactional
    public ArtistGroup createArtistGroup(MultipartFile file, ArtistGroupRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        Entertainment entertainment = entertainmentRepository.findByEnterName(request.getEnterName())
                .orElseThrow(() -> new CustomException(ENTERTAINMENT_NOT_FOUND));

        if (artistGroupRepository.findByGroupName(request.getGroupName()).isPresent()) {
            throw new CustomException(DUPLICATE_GROUP_NAME);
        }

        ArtistGroup artistGroup = new ArtistGroup(
                request.getGroupName(),
                request.getGroupInfo(),
                request.getEnterName(),
                entertainment
        );

        for (Long artistId : request.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new CustomException(ARTIST_NOT_FOUND));
            artistGroup.addArtist(artist);
        }

        artistGroupRepository.save(artistGroup);

        String imageUrl = START_PROFILE_URL;
        try {
            imageUrl = s3FileUploader.saveArtistGroupImage(file, artistGroup.getId());
        } catch (Exception e) {
            s3FileUploader.deleteFileInBucket(imageUrl);
            throw new S3Exception(UPLOAD_ERROR);
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(artistGroup.getId(), imageUrl);
        updateArtistGroupImageUrl(carrier);

        return artistGroup;
    }


    /**
     * 아티스트 그룹 DTO 조회
     *
     * @param groupName 그룹 이름
     * @return 아티스트 그룹 응답 DTO
     */
    public ArtistGroupResponseDto getArtistGroupResponseDto(String groupName) {
        ArtistGroup artistGroup = getArtistGroup(groupName);
        return convertToResponseDto(artistGroup);
    }

    /**
     * 모든 아티스트 그룹 DTO 조회
     *
     * @return 아티스트 그룹 응답 DTO 리스트
     */
    public List<ArtistGroupResponseDto> getAllArtistGroupResponseDtos() {
        List<ArtistGroup> artistGroups = getAllArtistGroups();
        return artistGroups.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * 아티스트그룹 검색기능
     *
     * @param groupName
     * @param page
     * @param size
     * @return
     */
    public Page<ArtistGroupResponseDto> searchArtistGroup(String groupName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // 전체 아티스트 그룹을 변환하는 부분
        Page<ArtistGroupResponseDto> allArtistGroup = artistGroupRepository.findAll(pageable)
                .map(this::convertToResponseDto);
        // 검색된 아티스트 그룹을 변환하는 부분
        List<ArtistGroupResponseDto> searchArtistGroup = artistGroupRepository.findByGroupNameContaining(groupName, pageable).stream()
                .map(this::convertToResponseDto)
                .toList();
        // 검색어가 없으면 전체를 반환, 아니면 검색 결과를 반환
        if (groupName.isEmpty()) {
            return allArtistGroup;
        }
        return new PageImpl<>(searchArtistGroup, pageable, searchArtistGroup.size());
    }

    /**
     * 아티스트 그룹 수정
     *
     * @param groupName 그룹 이름
     * @param request   요청 객체
     * @param user      로그인한 사용자 정보
     * @return 수정된 아티스트 그룹
     */
    @Transactional
    public ArtistGroup updateArtistGroup(String groupName, MultipartFile file,
                                         ArtistGroupRequestDto request, User user) {

        verifyEntertainmentOrAdminAuthority(user);
        ArtistGroup artistGroup = getArtistGroup(groupName);
        artistGroup.setGroupName(request.getGroupName());
        artistGroup.setGroupInfo(request.getGroupInfo());

        // artistIds가 null인지 확인하고, null이면 빈 리스트로 초기화
        if (request.getArtistIds() != null) {
            artistGroup.clearArtists();
            for (Long artistId : request.getArtistIds()) {
                Artist artist = artistRepository.findById(artistId)
                        .orElseThrow(() -> new CustomException(ARTIST_NOT_FOUND));
                artistGroup.addArtist(artist);
            }
        }

        if (isFileExists(file)) { // S3의 기존 이미지 삭제후 저장

            try {
                s3FileUploader.deleteFileInBucket(artistGroup.getArtistGroupProfileImageUrl());
            } catch (NotFoundException e) {
                artistGroup.updateImageUrl("");
                artistGroupRepository.save(artistGroup);
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }

            String newImageUrl;
            try {
                newImageUrl = s3FileUploader.saveArtistGroupImage(file, artistGroup.getId());
            } catch (Exception e) {
                s3FileUploader.deleteFileInBucket(artistGroup.getArtistGroupProfileImageUrl());
                throw new S3Exception(UPLOAD_ERROR);
            }

            ImageUrlCarrier carrier = new ImageUrlCarrier(artistGroup.getId(), newImageUrl);
            updateArtistGroupImageUrl(carrier);
        }

        return artistGroupRepository.save(artistGroup);
    }

    /**
     * 아티스트 그룹에서 아티스트 탈퇴
     *
     * @param groupName 그룹 이름
     * @param artistId  아티스트 ID
     * @param user      로그인한 사용자 정보
     */
    @Transactional
    public void removeArtistFromGroup(String groupName, Long artistId, User user) {
        verifyEntertainmentOrAdminAuthority(user);

        ArtistGroup artistGroup = getArtistGroup(groupName);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ARTIST_NOT_FOUND));

        if (artist.getArtistGroup() == null || !artist.getArtistGroup().equals(artistGroup)) {
            throw new CustomException(ARTIST_NOT_IN_GROUP);
        }

        // 그룹에서 아티스트 제거
        artistGroup.removeArtist(artist);

        // 변경된 엔티티 저장
        artistRepository.save(artist);
        artistGroupRepository.save(artistGroup);
    }


    /**
     * 아티스트 그룹 삭제
     *
     * @param groupName 그룹 이름
     * @param user      로그인한 사용자 정보
     */
    @Transactional
    public void deleteArtistGroup(String groupName, User user) {
        verifyEntertainmentOrAdminAuthority(user);

        ArtistGroup artistGroup = getArtistGroup(groupName);

        String imageUrl = user.getProfileImageUrl();
        if (!imageUrl.startsWith(BASIC_DIR)) {
            try {
                s3FileUploader.deleteFileInBucket(artistGroup.getArtistGroupProfileImageUrl());
            } catch (NotFoundException e) {
                artistGroup.updateImageUrl(START_PROFILE_URL);
                artistGroupRepository.save(artistGroup);
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }
        }
        artistGroupRepository.delete(artistGroup);
    }

    /**
     * 엔터테인먼트 권한 확인
     *
     * @param user 로그인한 사용자 정보
     */
    private void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * 엔터테인먼트 권한 확인
     *
     * @param user 로그인한 사용자 정보
     */
    private void verifyEntertainmentOrAdminAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole()) && !UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(UNAUTHORIZED_ACCESS);
        }
    }

    private void updateArtistGroupImageUrl(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrl().isEmpty()) {
            ArtistGroup artistGroup = artistGroupRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));
            artistGroup.updateImageUrl(carrier.getImageUrl());
        }
    }

    /**
     * 아티스트 그룹 조회
     *
     * @param groupName 그룹 이름
     * @return 아티스트 그룹
     */
    private ArtistGroup getArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new CustomException(ARTIST_GROUP_NOT_FOUND));
    }

    /**
     * 모든 아티스트 그룹 조회
     *
     * @return 아티스트 그룹 리스트
     */
    private List<ArtistGroup> getAllArtistGroups() {
        return artistGroupRepository.findAll();
    }

    /**
     * 아티스트 그룹 엔티티를 응답 DTO로 변환
     *
     * @param artistGroup 아티스트 그룹
     * @return 아티스트 그룹 응답 DTO
     */
    private ArtistGroupResponseDto convertToResponseDto(ArtistGroup artistGroup) {
        EntertainmentResponseDto entertainmentDto = new EntertainmentResponseDto(artistGroup.getEntertainment());
        List<ArtistResponseDto> artistDtos = artistGroup.getArtists().stream()
                .map(artist -> new ArtistResponseDto(artist.getId(), artist.getArtistName(), artist.getIntroduction(), artist.getArtistProfileImageUrl()))
                .collect(Collectors.toList());

        // 그룹 정보 추가
        return new ArtistGroupResponseDto(
                artistGroup.getId(),
                artistGroup.getGroupName(),
                artistGroup.getArtistGroupProfileImageUrl(),
                entertainmentDto,
                artistDtos,
                artistGroup.getEnterName(),
                artistGroup.getGroupInfo()
        );
    }

    /**
     * 아티스트 랭킹 조회
     * @return
     */
    public List<ArtistGroupResponseDto> getArtistRank() {
        List<ArtistGroup> artistGroups = artistGroupRepository.findTop15ArtistGroupsBySubscriptionCount();

        // 여기코드 너무 쓰레기같음..
        return artistGroups.stream().map(ag -> new ArtistGroupResponseDto(
                convertToResponseDto(ag),
                (long) ag.getSubscriptionList().size()
        )).collect(Collectors.toList());
    }
}