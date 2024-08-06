package com.example.fantreehouse.domain.feedlike.service;

import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.feedlike.dto.response.FeedLikeUserResponseDto;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final FeedLikeServiceSupport feedLikeServiceSupport;
    private final FeedLikeRepository feedLikeRepository;

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
        if (loginUser.getUserRole().equals(UserRoleEnum.ADMIN)
                || (loginUser.getUserRole().equals(UserRoleEnum.ENTERTAINMENT))) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        feedLikeServiceSupport.addOrCancelFeedLike(loginUser, artistFeedId, foundFeed);
    }

    public List<FeedLikeUserResponseDto> getUserAllFeedLikeUser(String groupName, Long artisFeedId, UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);

        List<FeedLike> feedLikeList = feedLikeRepository.findAllFeedLikeByFeedId(artisFeedId);
        if (feedLikeList.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_LIKE_USER);
        }

        feedLikeList.sort(Comparator.comparing(FeedLike::getCreatedAt).reversed());

        List<FeedLikeUserResponseDto> feedLikeUserResponseDtoList = new ArrayList<>();
        for (FeedLike feedLike : feedLikeList) {
            User user = feedLike.getUser();
            FeedLikeUserResponseDto feedLikeListResponseDto = FeedLikeUserResponseDto.of(user);
            feedLikeUserResponseDtoList.add(feedLikeListResponseDto);
        }

        return feedLikeUserResponseDtoList;
    }

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
