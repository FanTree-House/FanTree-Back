package com.example.fantreehouse.domain.artistgroup.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
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
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistGroupService {

    private final ArtistGroupRepository artistGroupRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistGroupService(ArtistGroupRepository artistGroupRepository, EntertainmentRepository entertainmentRepository, ArtistRepository artistRepository) {
        this.artistGroupRepository = artistGroupRepository;
        this.entertainmentRepository = entertainmentRepository;
        this.artistRepository = artistRepository;
    }

    /**
     * [createArtistGroup] 아티스트 그룹 생성
     * @param enterName 엔터테인먼트 이름
     * @param request 요청 객체
     * @param user 로그인한 사용자 정보
     * @return 생성된 아티스트 그룹
     */
    @Transactional
    public ArtistGroup createArtistGroup(String enterName, ArtistGroupRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        Entertainment entertainment = entertainmentRepository.findByEnterName(enterName)
                .orElseThrow(() -> new CustomException(ErrorType.ENTERTAINMENT_NOT_FOUND));

        ArtistGroup artistGroup = new ArtistGroup(request.getGroupName(), request.getArtistProfilePicture(), entertainment);

        for (Long artistId : request.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new CustomException(ErrorType.ARTIST_NOT_FOUND));
            artistGroup.addArtist(artist);
        }

        return artistGroupRepository.save(artistGroup);
    }

    /**
     * [getArtistGroupResponseDto] 아티스트 그룹 DTO 조회
     * @param enterName 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @return 아티스트 그룹 응답 DTO
     */
    public ArtistGroupResponseDto getArtistGroupResponseDto(String enterName, String groupName) {
        ArtistGroup artistGroup = getArtistGroup(enterName, groupName);
        return convertToResponseDto(artistGroup);
    }

    /**
     * [getAllArtistGroupResponseDtos] 모든 아티스트 그룹 DTO 조회
     * @param enterName 엔터테인먼트 이름
     * @return 아티스트 그룹 응답 DTO 리스트
     */
    public List<ArtistGroupResponseDto> getAllArtistGroupResponseDtos(String enterName) {
        List<ArtistGroup> artistGroups = getAllArtistGroups(enterName);
        return artistGroups.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * [updateArtistGroup] 아티스트 그룹 수정
     * @param enterName 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @param request 요청 객체
     * @param user 로그인한 사용자 정보
     * @return 수정된 아티스트 그룹
     */
    @Transactional
    public ArtistGroup updateArtistGroup(String enterName, String groupName, ArtistGroupRequestDto request, User user) {
        verifyEntertainmentOrAdminAuthority(user);
        ArtistGroup artistGroup = getArtistGroup(enterName, groupName);
        artistGroup.setGroupName(request.getGroupName());
        artistGroup.setArtistProfilePicture(request.getArtistProfilePicture());

        artistGroup.clearArtists();
        for (Long artistId : request.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new CustomException(ErrorType.ARTIST_NOT_FOUND));
            artistGroup.addArtist(artist);
        }

        return artistGroupRepository.save(artistGroup);
    }

    /**
     * [deleteArtistGroup] 아티스트 그룹 삭제
     * @param enterName 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @param user 로그인한 사용자 정보
     */
    @Transactional
    public void deleteArtistGroup(String enterName, String groupName, User user) {
        verifyEntertainmentOrAdminAuthority(user);

        ArtistGroup artistGroup = getArtistGroup(enterName, groupName);
        artistGroupRepository.delete(artistGroup);
    }

    /**
     * [verifyEntertainmentAuthority] 엔터테인먼트 권한 확인
     * @param user 로그인한 사용자 정보
     */
    private void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * [verifyEntertainmentAuthority] 엔터테인먼트 권한 확인
     * @param user 로그인한 사용자 정보
     */
    private void verifyEntertainmentOrAdminAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole()) && !UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }

    /**
     * [getArtistGroup] 아티스트 그룹 조회
     * @param enterName 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @return 아티스트 그룹
     */
    private ArtistGroup getArtistGroup(String enterName, String groupName) {
        return artistGroupRepository.findByEntertainmentEnterNameAndGroupName(enterName, groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));
    }

    /**
     * [getAllArtistGroups] 모든 아티스트 그룹 조회
     * @param enterName 엔터테인먼트 이름
     * @return 아티스트 그룹 리스트
     */
    private List<ArtistGroup> getAllArtistGroups(String enterName) {
        return artistGroupRepository.findAllByEntertainmentEnterName(enterName);
    }

    /**
     * [convertToResponseDto] 아티스트 그룹 엔티티를 응답 DTO로 변환
     * @param artistGroup 아티스트 그룹
     * @return 아티스트 그룹 응답 DTO
     */
    private ArtistGroupResponseDto convertToResponseDto(ArtistGroup artistGroup) {
        EntertainmentResponseDto entertainmentDto = new EntertainmentResponseDto(artistGroup.getEntertainment());
        List<ArtistResponseDto> artistDtos = artistGroup.getArtists().stream()
                .map(artist -> new ArtistResponseDto(artist.getId(), artist.getArtistName()))
                .collect(Collectors.toList());
        return new ArtistGroupResponseDto(artistGroup.getId(), artistGroup.getGroupName(), artistGroup.getArtistProfilePicture(), entertainmentDto, artistDtos);
    }
}