package com.example.fantreehouse.domain.artistgroup.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupRequestDto;
import com.example.fantreehouse.domain.artistgroup.dto.ArtistGroupResponseDto;
import com.example.fantreehouse.domain.artistgroup.service.ArtistGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.OVER_LOAD;

@RestController
@RequestMapping("/artistgroup")
@RequiredArgsConstructor
public class ArtistGroupController {

    private final ArtistGroupService artistGroupService;

//    @Autowired
//    public ArtistGroupController(ArtistGroupService artistGroupService) {
//        this.artistGroupService = artistGroupService;
//    }

    /**
     * [createArtistGroup] 아티스트 그룹 생성
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createArtistGroup(
            @RequestPart (value = "file") MultipartFile file,
            @Valid @ModelAttribute ArtistGroupRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new S3Exception(OVER_LOAD);
            }
        }
        artistGroupService.createArtistGroup(file, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_CREATE_SUCCESS));
    }

    /**
     * [getArtistGroup] 아티스트 그룹 조회
     * @param groupName 그룹 이름
     * @return 아티스트 그룹 응답 DTO
     */
    @GetMapping("/{groupName}")
    public ResponseEntity<ResponseDataDto<ArtistGroupResponseDto>> getArtistGroup(
            @PathVariable String groupName) {

        ArtistGroupResponseDto artistGroup = artistGroupService.getArtistGroupResponseDto(groupName);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroup));
    }

    /**
     * [getAllArtistGroups] 모든 아티스트 그룹 조회
     * @return 아티스트 그룹 응답 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<ResponseDataDto<List<ArtistGroupResponseDto>>> getAllArtistGroups() {
        List<ArtistGroupResponseDto> artistGroups = artistGroupService.getAllArtistGroupResponseDtos();
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_GROUP_RETRIEVE_SUCCESS, artistGroups));
    }

    /**
     * 아티스트그룹 검색 조회
     * @param groupName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDataDto<Page<ArtistGroupResponseDto>>> searchArtistGroup(
            @RequestParam String groupName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<ArtistGroupResponseDto> responseDto = artistGroupService.searchArtistGroup(groupName, page, size);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.ARTIST_READ_SUCCESS, responseDto));
    }


    /**
     * [updateArtistGroup] 아티스트 그룹 수정
     * @param groupName 그룹 이름
     * @param request 요청 객체
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @PatchMapping("/{groupName}")
    public ResponseEntity<ResponseMessageDto> updateArtistGroup(
            @PathVariable String groupName,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart ArtistGroupRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (file != null && file.getSize() > 10 * 1024 * 1024) {
            throw new S3Exception(OVER_LOAD);
        }
        artistGroupService.updateArtistGroup(groupName, file, request, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_UPDATE_SUCCESS));
    }

    /**
     * [removeArtistFromGroup] 아티스트 그룹에서 아티스트 탈퇴
     * @param groupName 그룹 이름
     * @param artistId 아티스트 ID
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{groupName}/artists/{artistId}")
    public ResponseEntity<ResponseMessageDto> removeArtistFromGroup(
            @PathVariable String groupName,
            @PathVariable Long artistId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        artistGroupService.removeArtistFromGroup(groupName, artistId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_REMOVE_SUCCESS));
    }

    /**
     * [deleteArtistGroup] 아티스트 그룹 삭제
     * @param groupName 그룹 이름
     * @param userDetails 로그인한 사용자 정보
     * @return 응답 메시지 DTO
     */
    @DeleteMapping("/{groupName}")
    public ResponseEntity<ResponseMessageDto> deleteArtistGroup(
            @PathVariable String groupName,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        artistGroupService.deleteArtistGroup(groupName, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.ARTIST_GROUP_DELETE_SUCCESS));
    }
}