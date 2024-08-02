package com.example.fantreehouse.domain.feedlike.service;

import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fantreehouse.common.enums.ErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final FeedLikeServiceSupport feedLikeServiceSupport;

    @Transactional
    public void addOrCancelLike(String groupName, Long artistFeedId, UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        //로그인 유저가 Feed 작성자 본인이면 좋아요 불가능할 경우 필요한 메서드
        User feedWriter = foundFeed.getUser();
        if (loginUser.getId().equals(feedWriter.getId())) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        //ADMIN 과 ENTER 도 좋아요를 누를 수 있는지 확인 필요 <- 누를 수 없을 경우 필터링 하는 메서드
        if(loginUser.getUserRole().equals(UserRoleEnum.ADMIN)
                || (loginUser.getUserRole().equals(UserRoleEnum.ENTERTAINMENT))){
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        feedLikeServiceSupport.addOrCancelFeedLike(loginUser, artistFeedId, foundFeed);
    }

//    public Page<FeedLikeResponseDto> getAllPersonalFeedLike(String groupName, UserDetailsImpl userDetails, int page) {
//
//        User loginUser = userDetails.getUser();
//        checkUserStatus(loginUser.getStatus());
//        existArtistGroup(groupName);
//
//        PageRequest pageRequest = PageRequest.of(page, FEED_LIKE_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<FeedLike> pagedFeed = feedLikeRepository.findPersonalFeedLikeAll(loginUser, pageRequest);
//
//        List<FeedLikeResponseDto> feedLikeResponseDtoList = pagedFeed.getContent().stream()
//                .map(feedLike -> FeedLikeResponseDto.of(feedLike.getFeed().getId(), feedLike.getFeed().getFeedLikeCount()))
//                .collect(Collectors.toList());
//
//        return PageableExecutionUtils.getPage(feedLikeResponseDtoList, pageRequest, pagedFeed::getTotalElements);
//    }

    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }



    private void existArtistGroup(String groupName) {
        artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));
    }
}
