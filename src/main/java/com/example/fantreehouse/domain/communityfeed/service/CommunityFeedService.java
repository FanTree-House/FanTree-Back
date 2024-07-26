package com.example.fantreehouse.domain.communityfeed.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.NOT_AVAILABLE_PERMISSION;

@Service
@RequiredArgsConstructor

public class CommunityFeedService {

    private final CommunityFeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional //피드생성
    public CommunityFeedResponseDto createFeed(CommunityFeedRequestDto requestDto, Long userId,String groupName) {
        // 활동유저검증
        User user = findUser(userId);
        checkUserStatus(user.getStatus());
        ArtistGroup artistGroup = findArtistGroup(groupName);
        //구독자와 아티스트그룹을 검증
        Subscription subscription = subscriptionRepository.findByUser_IdAndArtistGroup_Id(userId, artistGroup.getId()).orElseThrow(()
                -> new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS));

        //아티스트그룹을 구독했는지 검증
        CommunityFeed feed = new CommunityFeed(requestDto, subscription);
        feedRepository.save(feed);
        return new CommunityFeedResponseDto(feed);
    }

//    피드 전체 조회
    public List<CommunityFeedResponseDto> findAllFeed(Long userId, String gruopName) {
        User user = findUser(userId);
//        CommunityFeed feed = findFeed(feedId);
        ArtistGroup artistGroup = findArtistGroup(gruopName);
        checkUserStatus(user.getStatus());
        checkUserAccess(UserRoleEnum.USER, user);

        Subscription subscription = subscriptionRepository.findByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));
        if (!subscription.getArtistGroup().getId().equals(artistGroup.getId())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
        }
        List<CommunityFeed> feedList = feedRepository.findAll();
        if (feedList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_FEED);
        }
        return feedList.stream()
                .map(CommunityFeedResponseDto::new)
                .toList();
    }

    //피드 선택 조회
    public CommunityFeed findFeed(Long feedId, Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        checkUserStatus(user.getStatus());
        checkUserAccess(UserRoleEnum.USER, user);

        //구독자가 있는지 확인하는 로직
        Subscription subscription = subscriptionRepository.findByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));
        //구독자의 아티스트그룹ID가 변수 아티스트그룹의 ID와 일치하는지 검증
        if (!subscription.getArtistGroup().getId().equals(artistGroup.getId())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
        }

        CommunityFeed feed = feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
        return feed;
        }

    //피드 업데이트
    @Transactional
    public void updateFeed(CommunityFeedUpdateRequestDto requestDto, Long feedId, Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        checkUserStatus(user.getStatus());
        checkUserAccess(UserRoleEnum.USER, user);
        //구독자가 있는지 확인하는 로직
        Subscription subscription = subscriptionRepository.findByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));
        //구독자의 아티스트그룹ID가 변수 아티스트그룹의 ID와 일치하는지 검증
        if (!subscription.getArtistGroup().getId().equals(artistGroup.getId())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
        }
        CommunityFeed feed = feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));

        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_FEED);
        }
        feed.updateFeed(requestDto);
    }

    // 피드 삭제
    @Transactional
    public void deleteFeed(Long feedId, Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        checkUserStatus(user.getStatus());
        checkUserAccess(UserRoleEnum.USER, user);
        //구독자가 있는지 확인하는 로직
        Subscription subscription = subscriptionRepository.findByUserId(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_SUBSCRIPT_USER));
        //구독자의 아티스트그룹ID가 변수 아티스트그룹의 ID와 일치하는지 검증
        if (!subscription.getArtistGroup().getId().equals(artistGroup.getId())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
        }
        CommunityFeed feed = feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_FEED);
        }
        feedRepository.delete(feed);
    }

    //유저찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }

//피드찾기
    public CommunityFeed findFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }
    //아티스트 그룹찾기
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }
        //휴면유저 필터링
        private void checkUserStatus(UserStatusEnum userStatus) {
            if (!userStatus.equals(UserStatusEnum.ACTIVE_USER)) {
                throw new CustomException(NOT_AVAILABLE_PERMISSION);
            }
        }
        //유저롤과 구독을 동시검증
        private void checkUserAccess(UserRoleEnum userRoleEnum, User user) {
            if (userRoleEnum.equals(userRoleEnum.USER) && user.getSubscriptions().isEmpty()) {
                throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
            }
    }
}

