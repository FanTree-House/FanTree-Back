package com.example.fantreehouse.domain.communityfeed.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.service.CommunityFeedService;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.MAX_IMAGES_EXCEEDED;

@RestController
@RequestMapping("/artist/{groupName}/feeds")
@RequiredArgsConstructor

public class CommunityFeedController {

    private final CommunityFeedService feedService;

    /**
     * 커뮤니티 피드 생성
     * @param requestDto
     * @param userDetails
     * @param groupName
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createFeed(
        @RequestPart CommunityFeedRequestDto requestDto,
        @RequestPart(value = "file", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable String groupName
    ) {
        if (files != null && files.size() > 10) {
            throw new S3Exception(MAX_IMAGES_EXCEEDED);
        }
        feedService.createFeed(requestDto,files, userDetails.getUser().getId(), groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_FEED));
    }

    /**
     * 피드 전체 조회
     * @param userDetails
     * @param groupName
     * @return
     */
    @GetMapping
    public ResponseEntity<?> findAllFeed(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable String groupName) {

        List<CommunityFeedResponseDto> responseDto = feedService
            .findAllFeed(userDetails.getUser().getId(), groupName);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 피드 선택 조회
     * @param userDetails
     * @param feedId
     * @param groupName
     * @return
     */
    @GetMapping("/{feedId}")
     public ResponseEntity<?> findFeed(
         @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId,
        @PathVariable  String groupName) {

        CommunityFeed feed = feedService.findFeed(feedId ,userDetails.getUser().getId(),groupName);
        CommunityFeedResponseDto responseDto = new CommunityFeedResponseDto(feed);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 피드 수정
     * @param requestDto
     * @param userDetails
     * @param feedId
     * @param groupName
     * @return
     */
    @PatchMapping("/{feedId}")
    public ResponseEntity<ResponseMessageDto> updateFeed(
        @Valid @RequestPart CommunityFeedUpdateRequestDto requestDto,
        @RequestPart(value = "file", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId,
        @PathVariable String groupName
    ) {
        if (files != null && files.size() > 10) {
            throw new S3Exception(MAX_IMAGES_EXCEEDED);
        }
        feedService.updateFeed(requestDto, files, feedId, userDetails.getUser().getId(), groupName);
        return ResponseEntity
            .ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_UPDATE_SUCCESS));
    }

    /***
     * 커뮤니티 피드 삭제
     * @param feedId
     * @param userDetails
     * @param groupName
     * @return
     */
    @DeleteMapping("/{feedId}")
    public ResponseEntity<ResponseMessageDto> deleteFeed(
        @PathVariable Long feedId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable  String groupName,
        UserRoleEnum userRoleEnum) {

        feedService.deleteFeed(feedId, userDetails.getUser().getId(), groupName, userRoleEnum);
        return ResponseEntity
            .ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_DELETE_SUCCESS));
    }
}
