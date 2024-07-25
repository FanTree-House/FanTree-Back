package com.example.fantreehouse.domain.feed.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.response.CreateFeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.UpdateFeedResponseDto;
import com.example.fantreehouse.domain.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{groupName}")
public class FeedController {

    private final FeedService feedService;

    /**
     * Feed 생성
     *
     * @param groupName
//     * @param file
     * @param requestDto
     * @return
     * @throws IOException
     */
//    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PostMapping
    public ResponseEntity<ResponseDataDto<CreateFeedResponseDto>> createFeed(
            @PathVariable final String groupName,
//            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CreateFeedRequestDto requestDto
//    ) throws IOException {
    ) {

//        CreateFeedResponseDto responseDto = feedService.createFeed(groupName, userDetails, file, requestDto);
        CreateFeedResponseDto responseDto = feedService.createFeed(groupName, userDetails, requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_CREATED, responseDto));
    }

    /**
     * Feed 수정
     *
     * @param groupName
     * @param artistFeedId
//     * @param file
     * @param requestDto
     * @return
     * @throws IOException
     */
    @PatchMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<?>> updateFeed(
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
//            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final UpdateFeedRequestDto requestDto
//    ) throws IOException {
    ) {

//        UpdateFeedResponseDto responseDto = feedService.updateFeed(groupName, artistFeedId, userDetails, file, requestDto);
        UpdateFeedResponseDto responseDto = feedService.updateFeed(groupName, artistFeedId, userDetails, requestDto);

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_UPDATED, responseDto));
    }

    /**
     * Feed 단건 조회
     * @param groupName
     * @param artistFeedId
     * @return
     */
    @GetMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<?>> getFeed (
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FeedResponseDto responseDto = feedService.getFeed (groupName, artistFeedId, userDetails);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_READ_SUCCESS, responseDto));
    }

    /**
     * Feed 전체 조회
     * @param groupName
     * @param userDetails
     * @param page
     * @return
     */
    @GetMapping("/feeds")
    public ResponseEntity<ResponseDataDto<Page<FeedResponseDto>>> getAllFeed (
            @PathVariable final String groupName,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int page
    ) {
        Page<FeedResponseDto> pagedFeed = feedService.getAllFeed(groupName, userDetails, page);

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_READ_SUCCESS, pagedFeed));
    }

    /**
     * Feed 삭제
     * @param groupName
     * @param artistFeedId
     * @return
     */
    @DeleteMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseMessageDto> deleteFeed (
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl UserDetails
    ) {
        feedService.deleteFeed(groupName, artistFeedId, UserDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FEED_DELETED));
    }

}
