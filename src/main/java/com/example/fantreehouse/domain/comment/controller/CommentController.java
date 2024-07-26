package com.example.fantreehouse.domain.comment.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.comment.dto.CommentRequestDto;
import com.example.fantreehouse.domain.comment.dto.CommentResponseDto;
import com.example.fantreehouse.domain.comment.dto.request.CreateCommentRequestDto;
import com.example.fantreehouse.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/{feedId}")
public class CommentController {

    private final CommentService commentService;

    /**
     * 아티스트 feed 댓글 생성
     * @param feedId
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PostMapping("/comment")
    public ResponseEntity<ResponseMessageDto> createComment(
        @PathVariable Long feedId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody CreateCommentRequestDto requestDto
    ) {
        commentService.createComment(feedId, userDetails, requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_COMMENT));
    }

    /**
     * 아티스트 feed 댓글 수정
     * @param feedId
     * @param artistFeedCommentId
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PutMapping("/comment/{artistFeedCommentId}")
    public ResponseEntity<ResponseMessageDto> updateComment(
            @PathVariable Long feedId,
            @PathVariable Long artistFeedCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        commentService.updateComment(feedId, artistFeedCommentId, userDetails, requestDto);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_SUCCESS_COMMENT));
    }

    /**
     * 아티스트 feed 댓글 삭제
     * @param feedId
     * @param artistFeedCommentId
     * @param userDetails
     * @return
     */
    @DeleteMapping("/comment/{artistFeedCommentId}")
    public ResponseEntity<ResponseMessageDto> deleteComment(
            @PathVariable Long feedId,
            @PathVariable Long artistFeedCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.deleteComment(feedId, artistFeedCommentId, userDetails);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_SUCCESS_COMMENT));
    }

    /**
     * 아티스트 feed 댓글 (전체) 조회
     * @param feedId
     * @param userDetails
     * @param page
     * @return
     */
    @GetMapping
    public ResponseEntity<ResponseDataDto<Page<CommentResponseDto>>> getComment(
            @PathVariable Long feedId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam int page

    ) {
        Page<CommentResponseDto> pageComment = commentService.getAllComment(feedId, userDetails, page);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.READ_SUCCESS_COMMENT, pageComment));
    }


}
