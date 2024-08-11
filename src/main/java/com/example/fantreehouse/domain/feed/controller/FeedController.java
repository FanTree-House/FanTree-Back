package com.example.fantreehouse.domain.feed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.response.CreateFeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.UpdateFeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.service.FeedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.MAX_IMAGES_EXCEEDED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class FeedController {

    private final FeedService feedService;
    /**
     * Feed 생성
     *
     * @param groupName
     * @param files
     * @param requestDto
     * @return
     */
    @PostMapping("/{groupName}")
    public ResponseEntity<ResponseDataDto<CreateFeedResponseDto>> createFeed(
            @PathVariable final String groupName,
            @RequestPart(value = "file", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart final CreateFeedRequestDto requestDto
    ) {
        if (files != null && files.size() > 10) {
            throw new S3Exception(MAX_IMAGES_EXCEEDED);
        }
        CreateFeedResponseDto responseDto = feedService.createFeed(groupName, userDetails.getUser(), files, requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_CREATED, responseDto));
    }

    /**
     * Feed 수정
     *
     * @param groupName
     * @param artistFeedId
     * @param requestDto
     * @return
     */
    @PatchMapping("/{groupName}/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<UpdateFeedResponseDto>> updateFeed(
            @PathVariable final String groupName,
            @RequestPart(value = "file", required = false) List<MultipartFile> files,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart final UpdateFeedRequestDto requestDto
    ) {
        if (files != null && files.size() > 10) {
            throw new S3Exception(MAX_IMAGES_EXCEEDED);
        }
        UpdateFeedResponseDto responseDto = feedService.updateFeed(groupName, artistFeedId, userDetails, files, requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_UPDATED, responseDto));
    }

    /**
     * Feed 단건 조회
     *
     * @param groupName
     * @param artistFeedId
     * @return
     */
    @GetMapping("/{groupName}/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<FeedResponseDto>> getFeed(
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FeedResponseDto responseDto = feedService.getFeed(groupName, artistFeedId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_READ_SUCCESS, responseDto));
    }

    /**
     * Feed 전체 조회
     *
     * @param groupName
     * @param page
     * @return
     */
    @GetMapping("/{groupName}/feeds")
    public ResponseEntity<ResponseDataDto<Page<FeedResponseDto>>> getAllFeed(
            @PathVariable final String groupName,
            @RequestParam int page
    ) {
        Page<FeedResponseDto> pagedFeed = feedService.getAllFeed(groupName, page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_READ_SUCCESS, pagedFeed));
    }

    /**
     * Feed 삭제
     *
     * @param artistFeedId
     * @return
     */
    @DeleteMapping("/{groupName}/feed/{artistFeedId}")
    public ResponseEntity<ResponseMessageDto> deleteFeed(
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl UserDetails
    ) {
        feedService.deleteFeed(artistFeedId, UserDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FEED_DELETED));
    }

    //개인별 좋아요 누른 feed 모아보기
    @GetMapping("/feed/likes")
    public ResponseEntity<ResponseDataDto<List<FeedResponseDto>>> getLikeFeeds(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<FeedResponseDto> FeedResponseDtoList = feedService.getLikeFeeds(userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS_GET_FEED_LIKE_USERS, FeedResponseDtoList));
    }

}
