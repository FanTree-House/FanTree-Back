package com.example.fantreehouse.domain.communitycomment.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentResponseDto;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communitycomment.service.CommunityCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{group_name}/community/feeds/{community_feed_id}/comments")

public class CommunityCommentController {

    private final CommunityCommentService commentService;

    /**
     * 댓글생성기능
     * @param requestDto
     * @param userDetails
     * @param community_feed_id
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createCommnet(@Valid CommunityCommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("community_feed_id") Long community_feed_id) {
        commentService.createcComment(requestDto, userDetails.getUser(), community_feed_id);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_COMMENT));
    }

    /**
     * 댓글 전체조회
     * @param userDetails
     * @return
     */
    public ResponseEntity<?> findComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CommunityCommentResponseDto> responseDtos = commentService.findAllComment(userDetails.getUser());
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 댓글 수정 기능
     * @param requestDto
     * @param comment_id
     * @param userDetails
     * @return
     */
    @PatchMapping("/{comment_id}")
    public ResponseEntity<ResponseMessageDto> updateComment(@Valid CommunityCommentRequestDto requestDto,
                                                            @PathVariable Long comment_id,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommunityComment comment = commentService.updateComment(comment_id, requestDto, userDetails.getUser());
        CommunityCommentResponseDto responseDto = new CommunityCommentResponseDto(comment);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.UPDATE_SUCCESS_COMMENT));
    }

    /**
     * 댓글 삭제 기능
     * @param userDetails
     * @param comment_id
     * @return
     */
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<ResponseMessageDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long comment_id) {
        commentService.deleteComment(comment_id, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_SUCCESS_COMMENT));
    }

}








