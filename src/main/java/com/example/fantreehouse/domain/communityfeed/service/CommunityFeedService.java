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
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommunityFeedService {

    private final CommunityFeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;

    @Transactional //피드생성
    public CommunityFeedResponseDto createFeed(CommunityFeedRequestDto requestDto, User user, ArtistGroup artistGroup) {
        //아티스트그룹을 구독했는지 검증
        fanCheck(user, artistGroup);
        CommunityFeed feed = new CommunityFeed(requestDto, user);
        feedRepository.save(feed);
        return new CommunityFeedResponseDto(feed);
    }

//    피드 전체 조회
    public List<CommunityFeedResponseDto> findAllFeed(User user, ArtistGroup artistGroup) {
        fanCheck(user, artistGroup);
        List<CommunityFeed> feedList = feedRepository.findAllByUserId(user.getId());

        if (feedList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_FEED);
        }
        return feedList.stream()
                .map(CommunityFeedResponseDto::new)
                .toList();
    }

    //피드 선택 조회
    public CommunityFeed findFeed(Long community_feed_id, User user, ArtistGroup artistGroup) {
       fanCheck(user,artistGroup);
        CommunityFeed feed = feedRepository.findById(community_feed_id).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
        return feed;
        }

    //피드 업데이트
    @Transactional
    public void updateFeed(CommunityFeedUpdateRequestDto requestDto, Long community_feed_id, User user, ArtistGroup artistGroup) {
        fanCheck(user, artistGroup);
        CommunityFeed feed = feedRepository.findById(community_feed_id).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_FEED);
        }
        feed.updateFeed(requestDto);
    }

    // 피드 삭제
    @Transactional
    public void deleteFeed(Long community_feed_id, User user, ArtistGroup artistGroup) {
        fanCheck(user, artistGroup);
        CommunityFeed feed = feedRepository.findById(community_feed_id).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_FEED);
        }
        feedRepository.delete(feed);
    }

    //유저가 아티스트 그룹에 가입되어있는지 확인하는 로직
    public void fanCheck(User user, ArtistGroup artistGroup) {
//        user = userRepository.findById(user.getId()).orElseThrow(()
//                -> new CustomException(ErrorType.USER_NOT_FOUND));
//        artistGroup = artistGroupRepository.findById(artistGroup.getId()).orElseThrow(()
//                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
//        if (!user.getArtist().getArtistGroup().getId().equals(artistGroup.getId())) {
//            throw new CustomException(ErrorType.NOT_MATCH_USER);
//        }
    }
}
