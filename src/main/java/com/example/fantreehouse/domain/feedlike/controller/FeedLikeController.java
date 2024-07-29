package com.example.fantreehouse.domain.feedlike.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.feedlike.service.FeedLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{groupName}/feed/{artistFeedId}")
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    @PostMapping
    public ResponseEntity<ResponseMessageDto> addOrDeleteLike (
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        feedLikeService.addOrCancelLike(groupName, artistFeedId, userDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.FEED_LIKE_ADD_OR_NOT));
    }
    //Feed 의 좋아요를 누른 유저 List 보기 <- 최신순, 페이지네이션
}
