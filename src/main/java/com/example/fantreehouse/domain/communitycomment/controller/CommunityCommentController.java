package com.example.fantreehouse.domain.communitycomment.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentResponseDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentUpdateRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentUpdateResponseDto;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communitycomment.service.CommunityCommentService;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist/{groupName}/feeds/{feedId}/comments")

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
    public ResponseEntity<ResponseMessageDto> createCommnet(@Valid @RequestBody CommunityCommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long feedId) {
        commentService.createComment(requestDto, userDetails.getUser().getId(), feedId);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_COMMENT));
    }

    /**
     * 댓글전체조회
     * @param groupName
     * @param feedId
     * @return
     */
    @GetMapping
    public ResponseEntity<?> findComment(@PathVariable String groupName,
                                         Long feedId) {
        List<CommunityCommentResponseDto> responseDto = commentService.findAllComment(groupName, feedId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 수정 기능
     * @param requestDto
     * @param commentId
     * @param userDetails
     * @return
     */
    @Transactional
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@Valid @RequestBody CommunityCommentUpdateRequestDto requestDto,
                                                            @PathVariable Long commentId,
                                                            @PathVariable String groupName,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommunityComment comment = commentService.updateComment(commentId, requestDto, userDetails.getUser().getId(),groupName);
        CommunityCommentUpdateResponseDto responseDto = new CommunityCommentUpdateResponseDto(comment);
        return ResponseEntity.ok(responseDto);
//        return ResponseEntity.ok(responseDto);
    }

    /**
     * 댓글 삭제 기능
     * @param userDetails
     * @param commentId
     * @param groupName
     * @return
     */
    @Transactional
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessageDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long commentId,
                                                            @PathVariable String groupName) {
        commentService.deleteComment(commentId, userDetails.getUser().getId(), groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.DELETE_SUCCESS_COMMENT));
    }

}








