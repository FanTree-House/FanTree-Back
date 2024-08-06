package com.example.fantreehouse.domain.subscription.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.subscription.dto.SubscriptionResponseDto;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;

    //  구독생성
    @Transactional
    public void createSubscript(User user, String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);

        subscriptionRepository.findByUserIdAndArtistGroupId(user.getId(), artistGroup.getId()).ifPresent(e ->
                {
                    throw new CustomException(ErrorType.DUPLICATE_USER);
                });

        Subscription subscription = new Subscription(user, artistGroup);
        subscriptionRepository.save(subscription);
    }

    //구독해지
    @Transactional
    public void deleteSubscript(User user, String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);

        Subscription subscription = subscriptionRepository.findByUserIdAndArtistGroupId(user.getId(), artistGroup.getId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_SUBSCRIPT_USER));

        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.DUPLICATE_USER);
        }
        subscriptionRepository.delete(subscription);
    }

    //구독리스트 조회
    public List<SubscriptionResponseDto> findAllSubscript(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        List<Subscription> subscriptionList = subscriptionRepository.findAllByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));

        if (subscriptionList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_SUBSCRIPTION);
        }
        return subscriptionList.stream()
                .map(SubscriptionResponseDto::new)
                .toList();
    }

    //구독한 그룹의 피드 모두 보기(최신순)
    public List<FeedResponseDto> getSubscribedGroupFeeds(User user) {

        List<Subscription> subscriptions = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPTION));

        List<ArtistGroup> artistGroups = subscriptions.stream()
                .map(Subscription::getArtistGroup)
                .toList();

        List<Feed> subGroupFeeds = artistGroups.stream()
                .flatMap(artistGroup -> Optional.ofNullable(feedRepository.findByArtistGroupId(artistGroup.getId()))
                        .stream()
                        .flatMap(List::stream))
                .collect(Collectors.toList());

        if (subGroupFeeds.isEmpty()) {
            throw new NotFoundException(ErrorType.SUBSCRIPT_FEED_NOT_FOUND);
        }

        subGroupFeeds.sort(Comparator.comparing(Feed::getCreatedAt).reversed());

        //좋아요도 같이 담아서 반환
        List<FeedResponseDto> subsGroupFeedDtos = new ArrayList<>();
        for (Feed feed : subGroupFeeds) {
            //좋아요 개수 세기
            Long feedLikeCount = feedLikeRepository.countByFeedId(feed.getId());
            FeedResponseDto dto = FeedResponseDto.of(feed, feedLikeCount);
            subsGroupFeedDtos.add(dto);
        }

        return subsGroupFeedDtos;
    }

    // 아티스트 그룹을 구독했는 지
    public boolean getIsSubscribe(User user, String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        return subscriptionRepository.findByUserIdAndArtistGroupId(user.getId(), artistGroup.getId()).isPresent();
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }

}