package com.example.fantreehouse.domain.communityfeed.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.exception.errorcode.CommonErrorCode;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.communityfeed.service.CommunityFeedService;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.ResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/fantree/artist/feed")
@RequiredArgsConstructor
public class CommunityFeedController {

    private CommunityFeedService feedService;
    /***
     * 구독자 커뮤니티 생성
//     * @param requestDto
//     * @param userDetails
//     * @return
//     */
    @PostMapping
    public ResponseEntity<ResponseMessageDto> createFeed(@Valid @RequestBody CommunityFeedRequestDto requestDto
            /*Todo : @AuthenticationPrincipal UserDetailsImpl userDetails*/
    ) {
        User user = User.builder().build();
        feedService.createFeed(requestDto, user  /*Todo : userDetails.getUser() 유저값 받아오기*/ );
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.CREATRE_SUCCESS));
    }

    /***
     * 커뮤니티 피드 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<?> findAllFeed(/*Todo : @AuthenticationPrincipal UserDetailsImpl userDetails*/)
    {
        List<CommunityFeedResponseDto> responseDto = feedService.findAllFeed(/*Todo : userDetails.getUser() 유저값 받아오기*/ );
        return ResponseEntity.ok(responseDto);
    }

    /***
     * 커뮤니티 피드 선택 조회
     */
    @GetMapping("/{community_feed_id}")
     public ResponseEntity<?> findFeed(/*Todo : @AuthenticationPrincipal UserDetailsImpl userDetails*/
                                        @PathVariable Long community_feed_id)
    {
        CommunityFeed feed = feedService.findFeed(community_feed_id /*userDetails.getUser()*/);
//        CommunityFeedResponseDto responseDto = new CommunityFeedResponseDto(feed);
//        return ResponseEntity.ok(responseDto);
        return null;
    }
    /***
     * 커뮤니티 피드 수정
     * @param requestDto
     * @param community_feed_id
     * @return
     */
    @PatchMapping("/{community_feed_id}")
    public ResponseEntity<ResponseMessageDto> updateFeed(@Valid @RequestBody CommunityFeedUpdateRequestDto requestDto,
                                                            @PathVariable Long community_feed_id
            /*Todo : @AuthenticationPrincipal UserDetailsImpl userDetails*/
    ) {
        feedService.updateFeed(requestDto, community_feed_id /*Todo : userDetails.getUser() 유저값 받아오기*/);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_UPDATE_SUCCESS));
    }

    /***
     * 커뮤니티 피드삭제
     * @param community_feed_id
     * @return
     */
    @DeleteMapping("/{community_feed_id}")
    public ResponseEntity<ResponseMessageDto> DeleteFeed(@PathVariable Long community_feed_id
            /*Todo : @AuthenticationPrincipal UserDetailsImpl userDetails*/
    ) {
        feedService.deleteFeed(community_feed_id /*Todo : userDetails.getUser() 유저값 받아오기*/);
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.USER_COMMUNITY_DELETE_SUCCESS));
    }
}
