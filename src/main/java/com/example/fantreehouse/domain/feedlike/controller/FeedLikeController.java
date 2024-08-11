package com.example.fantreehouse.domain.feedlike.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.feedlike.dto.response.FeedLikeUserResponseDto;
import com.example.fantreehouse.domain.feedlike.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/{artistFeedId}")
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    /**
     * 아티스트 Feed 좋아요 추가 또는 취소
     *
     * @param artistFeedId
     * @param userDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> addOrDeleteLike(
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        feedLikeService.addOrCancelLike( artistFeedId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FEED_LIKE_CHANGED));
    }

    // 좋아요 유무
    @GetMapping("/check")
    public boolean getIsLiked(
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return feedLikeService.getIsLiked(artistFeedId, userDetails.getUser());
    }

    /**
     * Feed 의 좋아요를 누른 유저 List 보기 (최신순)
     *
     * @param artistFeedId
     * @param userDetails
     * @return
     */
    @GetMapping("/like")
    public ResponseEntity<ResponseDataDto<List<FeedLikeUserResponseDto>>> getUserAllFeedLikeUser(
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<FeedLikeUserResponseDto> feedLikeUserResponseDtoList = feedLikeService.getUserAllFeedLikeUser( artistFeedId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS_GET_FEED_LIKE_USERS, feedLikeUserResponseDtoList));
    }

    /**
     * 피드의 좋아요 수 조회
     *
     * @param artistFeedId
     * @return
     */
    @GetMapping("/likes")
    public Long getFeedLikes(
            @PathVariable final Long artistFeedId
    ) {
        return feedLikeService.getFeedLikes(artistFeedId);
    }
}
