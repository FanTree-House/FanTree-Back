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
import com.example.fantreehouse.domain.product.product.dto.ProductResponseDto;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistGroupService {

    private final ArtistGroupRepository artistGroupRepository;
    private final EntertainmentRepository entertainmentRepository;
    private final ArtistRepository artistRepository;

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


        if (artistGroupRepository.findByGroupName(request.getGroupName()).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_GROUP_NAME);
        }

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

//
//    public Page<ProductResponseDto> searchProduct(String productName, int page, int size) {
//        Page<ProductResponseDto> allProduct = productRepository.findAll(pageable).map(ProductResponseDto::new);
//        List<ProductResponseDto> searchProduct = productRepository.findByProductNameContaining(productName, pageable).stream()
//                .map(ProductResponseDto::new).toList();
//        if (productName.isEmpty()) {
//            return allProduct;
//        } return new PageImpl<>(searchProduct);

    /**
     * 아티스트그룹 검색기능
     * @param groupName
     * @param page
     * @param size
     * @return
     */
    public Page<ArtistGroupResponseDto> searchArtistGroup(String groupName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<ArtistGroupResponseDto> allArtistGroup = artistGroupRepository.findAll(pageable).map(ArtistGroupResponseDto::new);
        List<ArtistGroupResponseDto> searchArtistGroup = artistGroupRepository.findByGroupNameContaining(groupName, pageable).stream()
                .map(ArtistGroupResponseDto::new).toList();
        if (groupName.isEmpty()) {
            return allArtistGroup;
        } return new PageImpl<>(searchArtistGroup);
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
     * [removeArtistFromGroup] 아티스트 그룹에서 아티스트 탈퇴
     * @param enterName 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @param artistId 아티스트 ID
     * @param user 로그인한 사용자 정보
     */
    @Transactional
    public void removeArtistFromGroup(String enterName, String groupName, Long artistId, User user) {
        verifyEntertainmentOrAdminAuthority(user);

        ArtistGroup artistGroup = getArtistGroup(enterName, groupName);
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_NOT_FOUND));

        if (artist.getArtistGroup() == null || !artist.getArtistGroup().equals(artistGroup)) {
            throw new CustomException(ErrorType.ARTIST_NOT_IN_GROUP);
        }

        // 그룹에서 아티스트 제거
        artistGroup.removeArtist(artist);

        // 변경된 엔티티 저장
        artistRepository.save(artist);
        artistGroupRepository.save(artistGroup);
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