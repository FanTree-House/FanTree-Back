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
@RequestMapping("/artist/{groupName}/feeds/{feedId}")
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
    @PostMapping("/like")
    public ResponseEntity<ResponseMessageDto> pressFeedLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long feedId, @PathVariable String groupName) {
        likeService.pressFeedLike(userDetails.getUser().getId(), feedId, groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.SUCCESS_FEED_LIKE));
    }

    /**
     * 피드 좋아요 취소 기능
     * @param userDetails
     * @param feedId
     * @param groupName
     * @return
     */
    @DeleteMapping("/like")
    public ResponseEntity<ResponseMessageDto> pressFeedIsLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable Long feedId, @PathVariable String groupName) {
        likeService.pressFeedIsLike(userDetails.getUser().getId(), feedId, groupName);
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
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<ResponseMessageDto> pressCommentLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
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
    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<ResponseMessageDto> pressCommentIsLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long feedId, @PathVariable String groupName,
                                                                 @PathVariable Long commentId) {
        likeService.pressCommentIsLike(userDetails.getUser().getId(), feedId, groupName, commentId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_COMMENT_LIKE));
    }

}