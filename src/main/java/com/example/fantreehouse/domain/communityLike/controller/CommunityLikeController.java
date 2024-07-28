package com.example.fantreehouse.domain.communityLike.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.service.CommunityLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artist/{groupName}/feeds/{feedId}")
@RequiredArgsConstructor

public class CommunityLikeController {
    private final CommunityLikeService likeService;

    @PostMapping
    public ResponseEntity<ResponseMessageDto> pressFeedLike(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long feedId, @PathVariable String groupName) {
        CommunityLike communityLike = likeService.pressFeedLike(userDetails.getUser().getId(), feedId, groupName);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_FEED));
    }
}
