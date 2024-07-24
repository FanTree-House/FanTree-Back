package com.example.fantreehouse.domain.artistgroup.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupRequestDto;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupResponseDto;
import com.example.fantreehouse.domain.artistgroup.service.ArtistGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entertainments")
public class ArtistGroupController {

    private final ArtistGroupService artistGroupService;

    @Autowired
    public ArtistGroupController(ArtistGroupService artistGroupService) {
        this.artistGroupService = artistGroupService;
    }

    /**
     * [createArtistGroup] 아티스트 그룹 생성
     * @param entername 엔터테인먼트 이름
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PostMapping("/{entername}")
    public ResponseEntity<ResponseMessageDto> createArtistGroup(
            @PathVariable String entername,
            @RequestBody ArtistGroupRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        artistGroupService.createArtistGroup(entername, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_CREATE_SUCCESS));
    }

    /**
     * [getArtistGroup] 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @return 아티스트 그룹 응답 DTO
     */
    @GetMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseDataDto<ArtistGroupResponseDto>> getArtistGroup(
            @PathVariable String entername,
            @PathVariable String groupName) {

        ArtistGroupResponseDto artistGroup = artistGroupService.getArtistGroupResponseDto(entername, groupName);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroup));
    }

    /**
     * [getAllArtistGroups] 모든 아티스트 그룹 조회
     * @param entername 엔터테인먼트 이름
     * @return 아티스트 그룹 응답 DTO 리스트
     */
    @GetMapping("/{entername}/groupName")
    public ResponseEntity<ResponseDataDto<List<ArtistGroupResponseDto>>> getAllArtistGroups(@PathVariable String entername) {
        List<ArtistGroupResponseDto> artistGroups = artistGroupService.getAllArtistGroupResponseDtos(entername);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroups));
    }

    /**
     * [updateArtistGroup] 아티스트 그룹 수정
     * @param entername 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PatchMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseMessageDto> updateArtistGroup(
            @PathVariable String entername,
            @PathVariable String groupName,
            @RequestBody ArtistGroupRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        artistGroupService.updateArtistGroup(entername, groupName, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_UPDATE_SUCCESS));
    }

    /**
     * [deleteArtistGroup] 아티스트 그룹 삭제
     * @param entername 엔터테인먼트 이름
     * @param groupName 그룹 이름
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{entername}/{groupName}")
    public ResponseEntity<ResponseMessageDto> deleteArtistGroup(
            @PathVariable String entername,
            @PathVariable String groupName,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        artistGroupService.deleteArtistGroup(entername, groupName, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_DELETE_SUCCESS));
    }
}