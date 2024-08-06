package com.example.fantreehouse.domain.subscription.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.subscription.dto.SubscriptionResponseDto;
import com.example.fantreehouse.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artistGroup/subscript")
@RequiredArgsConstructor

public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    /**
     * 구독하기
     * @param userDetails
     * @param groupName
     * @return
     */
    @PostMapping("/{groupName}")
    public ResponseEntity<ResponseMessageDto> createSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String groupName) {
        subscriptionService.createSubscript(userDetails.getUser(), groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_SUCCESS_SUBSCRIPT));
    }

    /**
     * 구독해지하기
     * @param userDetails
     * @param groupName
     * @return
     */
    @DeleteMapping("/{groupName}")
    public ResponseEntity<ResponseMessageDto> deleteSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable String groupName) {
        subscriptionService.deleteSubscript(userDetails.getUser(), groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_DELETE_SUBSCRIPT));
    }

    //구독리스트조회
    @GetMapping
    public ResponseEntity<?> findAllSubscript(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SubscriptionResponseDto> responseDto = subscriptionService.findAllSubscript(userDetails.getUser().getId());
        return ResponseEntity.ok(responseDto);
    }

    //구독한 그룹 피드 모아보기
    @GetMapping("/feeds")//임시 api 주소
    public ResponseEntity<ResponseDataDto<List<FeedResponseDto>>> getSubscriptGroupFeeds(@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<FeedResponseDto> subscriptGroupFeeds = subscriptionService.getSubscribedGroupFeeds(userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.FEED_READ_SUCCESS, subscriptGroupFeeds));
    }

    // 아티스트 그룹을 구독했는 지
    @GetMapping("/{groupName}")
    public boolean getIsSubscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @PathVariable String groupName) {
        return subscriptionService.getIsSubscribe(userDetails.getUser(), groupName);
    }

}
