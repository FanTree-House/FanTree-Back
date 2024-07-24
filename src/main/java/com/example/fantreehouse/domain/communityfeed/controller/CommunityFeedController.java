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
     * 커뮤니티 피드 전체조회
     * @param userDetails
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
     * @param community_feed_id
     * @param artistGroup
     * @return
     */
    @GetMapping("/{community_feed_id}")
     public ResponseEntity<?> findFeed(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long community_feed_id,
                                       ArtistGroup artistGroup) {
        CommunityFeed feed = feedService.findFeed(community_feed_id ,userDetails.getUser(),artistGroup);
        CommunityFeedResponseDto responseDto = new CommunityFeedResponseDto(feed);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 커뮤니티 피드 수정
     * @param requestDto
     * @param userDetails
     * @param community_feed_id
     * @param artistGroup
     * @return
     */
    @PatchMapping("/{community_feed_id}")
    public ResponseEntity<ResponseMessageDto> updateFeed(@Valid CommunityFeedUpdateRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long community_feed_id, ArtistGroup artistGroup) {
        feedService.updateFeed(requestDto, community_feed_id, userDetails.getUser(), artistGroup);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_UPDATE_SUCCESS));
    }

    /***
     * 커뮤니티 피드 삭제
     * @param community_feed_id
     * @param userDetails
     * @param artistGroup
     * @return
     */
    @DeleteMapping("/{community_feed_id}")
    public ResponseEntity<ResponseMessageDto> DeleteFeed(@PathVariable Long community_feed_id,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         ArtistGroup artistGroup) {
        feedService.deleteFeed(community_feed_id, userDetails.getUser(), artistGroup);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_DELETE_SUCCESS));
    }
}
