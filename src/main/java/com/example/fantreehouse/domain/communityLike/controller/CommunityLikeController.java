package com.example.fantreehouse.domain.communityLike.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.service.CommunityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artist")
@RequiredArgsConstructor

public class CommunityLikeController {
    private final CommunityLikeService likeService;

    /**
     * 피드 좋아요 기능
     * @param userDetails
     * @param feedId
     * @param groupName
     * @return
     */
    @PostMapping("/{groupName}/feeds/{feedId}/like")
    public ResponseEntity<ResponseMessageDto> pressFeedLike(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId, @PathVariable String groupName) {
        likeService.pressFeedLike(userDetails.getUser().getId(), feedId, groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS_FEED_LIKE));
    }

    //피드 좋아요 (프론트 적용)
    @PostMapping("/cancelLike/{communityFeedId}")
    public ResponseEntity<ResponseMessageDto> addFeedLike(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long communityFeedId
    ) {
        likeService.addFeedLike(userDetails.getUser().getId(), communityFeedId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS_FEED_LIKE));
    }

    /**
     * 피드 좋아요 취소 기능
     * @param userDetails
     * @param feedId
     * @param groupName
     * @return
     */
    @DeleteMapping("/{groupName}/feeds/{feedId}/like")
    public ResponseEntity<ResponseMessageDto> pressFeedIsLike(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId, @PathVariable String groupName) {
        likeService.pressFeedIsLike(userDetails.getUser().getId(), feedId, groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_FEED_LIKE));
    }

    //커뮤피드 좋아요 취소 기능 (groupName 없는 ver.)
    @DeleteMapping("/cancelLike/{communityFeedId}")
    public ResponseEntity<ResponseMessageDto> cancelFeedLike(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long communityFeedId
    ) {
        likeService.cancelFeedLike(userDetails.getUser().getId(), communityFeedId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_FEED_LIKE));
    }

    /***
     * 댓글 좋아요 기능
      * @param userDetails
     * @param feedId
     * @param groupName
     * @param commentId
     * @return
     */
    @PostMapping("/{groupName}/feeds/{feedId}/comments/{commentId}/like")
    public ResponseEntity<ResponseMessageDto> pressCommentLike(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId, @PathVariable String groupName,
        @PathVariable Long commentId) {
        likeService.pressCommentLike(userDetails.getUser().getId(), feedId, groupName, commentId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS_COMMENT_LIKE));
    }

    /**
     * 댓글 좋아요 취소기능
     * @param userDetails
     * @param feedId
     * @param groupName
     * @param commentId
     * @return
     */
    @DeleteMapping("/{groupName}/feeds/{feedId}/comments/{commentId}/like")
    public ResponseEntity<ResponseMessageDto> pressCommentIsLike(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long feedId, @PathVariable String groupName,
        @PathVariable Long commentId) {
        likeService.pressCommentIsLike(userDetails.getUser().getId(), feedId, groupName, commentId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_COMMENT_LIKE));
    }

    // 좋아요 유무
    @GetMapping("/check/{communityFeedId}")
    public boolean getIsLiked(
            @PathVariable final Long communityFeedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return likeService.getIsLiked(communityFeedId, userDetails.getUser());
    }

}