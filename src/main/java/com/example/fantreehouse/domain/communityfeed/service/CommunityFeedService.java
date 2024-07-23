package com.example.fantreehouse.domain.communityfeed.service;

import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@NoArgsConstructor
public class CommunityFeedService {

    public CommunityFeedRepository feedRepository;
    public UserRepository userRepository;
    public ArtistGroupRepository artistGroupRepository;

    @Transactional //피드생성
    public void createFeed(CommunityFeedRequestDto requestDto /*Todo : User user*/) {
// Todo :  user = userRepository.findByid(user.getId).orElseThrow(()
//  -> new 오류코드)
//        유저가 가진 아티스트그룹 키 값이 아티스트그룹레포지토리에 있는지 검증후 없다면 예외터트리기
//       2차검증이 끝나면 커뮤니티피드를 생성하는 생성자로직 + 레포지토리에 세이브
    }

    //피드 전체 조회
    public List<CommunityFeedResponseDto> findAllFeed(/*User user*/) {
        List<CommunityFeed> feedList = feedRepository.findAllById(/*Todo : 유저검증*/);
        if (feedList.isEmpty()) {
//                throw Custom (.NOT_FOUND_USER_FEED);
        }
        return feedList.stream()
                .map(CommunityFeedResponseDto::new)
                .toList();
    }

    //피드 선택 조회
    public CommunityFeed findFeed(Long communityFeedId  /*User user*/) {
        CommunityFeed feed = feedRepository.findById(communityFeedId).orElseThrow(()
                -> new IllegalArgumentException("피드가 존재하지 않습니다")); // Todo : custom으로 변경

        // 피드가 가진 유저아이디가 조회하는 유저아이디와 일치하는지 검증필요
        return feed;
    }

    @Transactional //피드 업데이트
    public void updateFeed(CommunityFeedUpdateRequestDto requestDto, Long community_feed_id) {
//        Todo :  user = userRepository.findByid(user.getId).orElseThrow(()
//         -> new 오류코드)
//        Todo : 커뮤니티 피드 레포지토리에서 피드가있나 검증하는 로직
//         ->있다면 유저가가진 커뮤니티ID와 대조후 Update -> save -> return
//
    }

    @Transactional // 피드 삭제
    public void deleteFeed(Long communityFeedId) {
        //        Todo :  user = userRepository.findByid(user.getId).orElseThrow(()
//         -> new 오류코드)
//        Todo : 커뮤니티 피드 레포지토리에서 피드가있나 검증하는 로직
//         ->있다면 유저가가진 커뮤니티ID와 대조후 Update -> save -> return
//            communityFeedRepository.delete(communityFeed)
    }
}
