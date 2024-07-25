package com.example.fantreehouse.domain.communityfeed.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.service.CommunityFeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fantree/artist/feeds")
@RequiredArgsConstructor

public class CommunityFeedController {

    private final CommunityFeedService feedService;

    /**
     * 커뮤니티 피드 생성
     * @param requestDto
     * @param userDetails
     * @param artistGroup
     * @return
     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createFeed(@Valid CommunityFeedRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         ArtistGroup artistGroup) {
        feedService.createFeed(requestDto, userDetails.getUser(), artistGroup);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATE_SUCCESS_FEED));
    }

    /**
     * 피드 전체 조회
     * @param userDetails
     * @param artistGroup
     * @return
     */
    @GetMapping
    public ResponseEntity<?> findAllFeed(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         ArtistGroup artistGroup) {
        List<CommunityFeedResponseDto> responseDto = feedService.findAllFeed(userDetails.getUser(),artistGroup);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 커뮤니티 피드 선택 조회
     * @param userDetails
     * @param feedId
     * @param artistGroup
     * @return
     */
    @GetMapping("/{feedId}")
     public ResponseEntity<?> findFeed(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long feedId,
                                       ArtistGroup artistGroup) {
        CommunityFeed feed = feedService.findFeed(feedId ,userDetails.getUser(),artistGroup);
        CommunityFeedResponseDto responseDto = new CommunityFeedResponseDto(feed);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 커뮤니티 피드 수정
     * @param requestDto
     * @param userDetails
     * @param feedId
     * @param artistGroup
     * @return
     */
    @PatchMapping("/{feed_id}")
    public ResponseEntity<ResponseMessageDto> updateFeed(@Valid CommunityFeedUpdateRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long feedId, ArtistGroup artistGroup) {
        feedService.updateFeed(requestDto, feedId, userDetails.getUser(), artistGroup);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_UPDATE_SUCCESS));
    }

    /***
     * 커뮤니티 피드 삭제
     * @param feedId
     * @param userDetails
     * @param artistGroup
     * @return
     */
    @DeleteMapping("/{feed_id}")
    public ResponseEntity<ResponseMessageDto> deleteFeed(@PathVariable Long feedId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         ArtistGroup artistGroup) {
        feedService.deleteFeed(feedId, userDetails.getUser(), artistGroup);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_DELETE_SUCCESS));
    }
}
