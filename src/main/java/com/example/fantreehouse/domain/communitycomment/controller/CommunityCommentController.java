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
@RequestMapping("/{group_name}/community/feeds/{feeId}/comments")

public class CommunityCommentController {

    private final CommunityCommentService commentService;

    /**
     * 댓글생성기능
     * @param requestDto
     * @param userDetails
     * @param feedId
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createCommnet(@Valid CommunityCommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable("feedId") Long feedId) {
        commentService.createcComment(requestDto, userDetails.getUser(), feedId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_COMMENT));
    }

    /**
     * 댓글 전체조회
     * @param userDetails
     * @return
     */
    @GetMapping
    public ResponseEntity<?> findComment(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CommunityCommentResponseDto> responseDto = commentService.findAllComment(userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 수정 기능
     * @param requestDto
     * @param commentId
     * @param userDetails
     * @return
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@Valid CommunityCommentRequestDto requestDto,
                                                            @PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommunityComment comment = commentService.updateComment(commentId, requestDto, userDetails.getUser());
        CommunityCommentResponseDto responseDto = new CommunityCommentResponseDto(comment);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 삭제 기능
     * @param userDetails
     * @param commentId
     * @return
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessageDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long commentId) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_SUCCESS_COMMENT));
    }

}








