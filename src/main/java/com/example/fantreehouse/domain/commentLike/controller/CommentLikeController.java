package com.example.fantreehouse.domain.commentLike.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.commentLike.service.CommentLikeService;
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
@RequestMapping("/{groupName}/feed/{artistFeedId}/comment/{artistFeedCommentId}")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping
    public ResponseEntity<ResponseMessageDto> addOrDeleteLike (
            @PathVariable final String groupName,
            @PathVariable final Long artistFeedId,
            @PathVariable final Long artistFeedCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentLikeService.addOrCancelLike(groupName, artistFeedId, artistFeedCommentId, userDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.COMMENT_LIKE_CHANGED));
    }

}
