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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/www.fantree.com/{group_name}")
public class FeedController {

    private final FeedService feedService;

    /**
     * Feed 생성
     *
     * @param group_name
     * @param file
     * @param requestDto
     * @return
     * @throws IOException
     */

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseDataDto<?>> createFeed(
            @PathVariable final String group_name,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CreateFeedRequestDto requestDto
    ) throws IOException {

        CreateFeedResponseDto responseDto = feedService.createFeed(group_name, userDetails, file, requestDto);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.CREATED, responseDto));
    }

    /**
     * Feed 수정
     *
     * @param group_name
     * @param artistFeedId
     * @param file
     * @param requestDto
     * @return
     * @throws IOException
     */

    @PatchMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<?>> updateFeed(
            @PathVariable final String group_name,
            @PathVariable final Long artistFeedId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final UpdateFeedRequestDto requestDto
    ) throws IOException {

        UpdateFeedResponseDto responseDto = feedService.updateFeed(group_name, artistFeedId, userDetails, file, requestDto);

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.UPDATED, responseDto));
    }

    /**
     * Feed 조회
     * @param group_name
     * @param artistFeedId
     * @return
     */

    @GetMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseDataDto<?>> getFeed (
            @PathVariable final String group_name,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        FeedResponseDto responseDto = feedService.getFeed (group_name, artistFeedId, userDetails);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, responseDto));
    }


    //Feed 전체 조회
    @GetMapping("/feed/feeds")
    public ResponseEntity<ResponseDataDto<?>> getAllFeed (
            @PathVariable final String group_name,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        FeedResponseDto responseDto = feedService.getAllFeed (group_name, userDetails);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS, responseDto));
    }

    /**
     * Feed 삭제
     * @param group_name
     * @param artistFeedId
     * @return
     */
    @DeleteMapping("/feed/{artistFeedId}")
    public ResponseEntity<ResponseMessageDto> deleteFeed (
            @PathVariable final String group_name,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl UserDetails
    ) {
        feedService.deleteFeed(group_name, artistFeedId, UserDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETED));
    }

}
