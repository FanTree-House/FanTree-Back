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
     * @param entername 엔터테인먼트 이름
     * @param request 요청 객체 (ArtistGroupRequestDto)
     * @return 생성된 ArtistGroup 객체
     * @throws CustomException 엔터테인먼트 또는 아티스트를 찾을 수 없는 경우 예외 발생
     **/
    @Transactional
    public ArtistGroup createArtistGroup(String entername, ArtistGroupRequestDto request) {
        Entertainment entertainment = entertainmentRepository.findByEntername(entername)
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
     * [getArtistGroupResponseDto] 특정 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @param groupName 아티스트 그룹 이름
     * @return 조회된 ArtistGroupResponseDto 객체
     * @throws CustomException 아티스트 그룹을 찾을 수 없는 경우 예외 발생
     **/
    public ArtistGroupResponseDto getArtistGroupResponseDto(String entername, String groupName) {
        ArtistGroup artistGroup = getArtistGroup(entername, groupName);
        return convertToResponseDto(artistGroup);
    }

    /**
     * [getAllArtistGroupResponseDtos] 특정 엔터테인먼트의 모든 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @return 조회된 모든 ArtistGroupResponseDto 객체의 리스트
     **/
    public List<ArtistGroupResponseDto> getAllArtistGroupResponseDtos(String entername) {
        List<ArtistGroup> artistGroups = getAllArtistGroups(entername);
        return artistGroups.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * [updateArtistGroup] 특정 아티스트 그룹 정보 업데이트
     * @param entername 엔터테인먼트 이름
     * @param groupName 아티스트 그룹 이름
     * @param request 업데이트 요청 객체 (ArtistGroupRequestDto)
     * @return 업데이트된 ArtistGroup 객체
     * @throws CustomException 아티스트 그룹 또는 아티스트를 찾을 수 없는 경우 예외 발생
     **/
    @Transactional
    public ArtistGroup updateArtistGroup(String entername, String groupName, ArtistGroupRequestDto request) {
        ArtistGroup artistGroup = getArtistGroup(entername, groupName);
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
     * [deleteArtistGroup] 특정 아티스트 그룹 삭제
     * @param entername 엔터테인먼트 이름
     * @param groupName 아티스트 그룹 이름
     * @throws CustomException 아티스트 그룹을 찾을 수 없는 경우 예외 발생
     **/
    @Transactional
    public void deleteArtistGroup(String entername, String groupName) {
        ArtistGroup artistGroup = getArtistGroup(entername, groupName);
        artistGroupRepository.delete(artistGroup);
    }

    /**
     * [getArtistGroup] 특정 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @param groupName 아티스트 그룹 이름
     * @return 조회된 ArtistGroup 객체
     * @throws CustomException 아티스트 그룹을 찾을 수 없는 경우 예외 발생
     **/
    public ArtistGroup getArtistGroup(String entername, String groupName) {
        return artistGroupRepository.findByEntertainmentEnternameAndGroupName(entername, groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));
    }

    /**
     * [getAllArtistGroups] 특정 엔터테인먼트의 모든 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @return 조회된 모든 ArtistGroup 객체의 리스트
     **/
    public List<ArtistGroup> getAllArtistGroups(String entername) {
        return artistGroupRepository.findAllByEntertainmentEntername(entername);
    }

    /**
     * [convertToResponseDto] ArtistGroup 엔터티를 ArtistGroupResponseDto로 변환
     * @param artistGroup 변환할 ArtistGroup 엔터티
     * @return 변환된 ArtistGroupResponseDto 객체
     **/
    private ArtistGroupResponseDto convertToResponseDto(ArtistGroup artistGroup) {
        EntertainmentResponseDto entertainmentDto = new EntertainmentResponseDto(artistGroup.getEntertainment().getId(), artistGroup.getEntertainment().getEntername());
        List<ArtistResponseDto> artistDtos = artistGroup.getArtists().stream()
                .map(artist -> new ArtistResponseDto(artist.getId(), artist.getArtistName()))
                .collect(Collectors.toList());
        return new ArtistGroupResponseDto(artistGroup.getId(), artistGroup.getGroupName(), artistGroup.getArtistProfilePicture(), entertainmentDto, artistDtos);
    }
}