package com.example.fantreehouse.domain.commentLike.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.commentLike.dto.CommentLikeResponseDto;
import com.example.fantreehouse.domain.commentLike.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    /**
     * 좋아요 생성
     * @param groupName
     * @param artistFeedId
     * @param artistFeedCommentId
     * @param userDetails
     */
    @PostMapping("/{groupName}/feed/{artistFeedId}/comment/{artistFeedCommentId}")
    public ResponseEntity<ResponseMessageDto> addOrDeleteLike (
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @PathVariable final Long artistFeedCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentLikeService.addOrCancelLike(groupName, artistFeedId, artistFeedCommentId, userDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.COMMENT_LIKE_CHANGED));
    }

    /**
     * 좋아요 개수 조회
     * @param artistFeedCommentId
     * @return
     */
    @GetMapping("/feed/comment/{artistFeedCommentId}/LikeCount")
    public ResponseEntity<ResponseDataDto<CommentLikeResponseDto>> getLikeCount(
            @PathVariable final Long artistFeedCommentId
    ) {
        CommentLikeResponseDto responseDto = commentLikeService.getLikeCount(artistFeedCommentId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS_GET_COMMENT_LIKE_COUNT, responseDto));
    }

    /**
     * 좋아요 유무 조회
     * @param artistFeedCommentId
     * @param userDetails
     * @return
     */
    @GetMapping("/feed/comment/{artistFeedCommentId}/like")
    public ResponseEntity<ResponseDataDto<CommentLikeResponseDto>> getIsLiked(
            @PathVariable final Long artistFeedCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentLikeResponseDto responseDto = commentLikeService.getIsLiked(artistFeedCommentId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.SUCCESS_GET_COMMENT_ISLIKED, responseDto));
    }
}
