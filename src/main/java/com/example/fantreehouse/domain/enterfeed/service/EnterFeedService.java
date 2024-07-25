package com.example.fantreehouse.domain.enterfeed.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnterFeedService {

    private final EnterFeedRepository enterFeedRepository;
    private final ArtistGroupRepository artistGroupRepository;

    @Autowired
    public EnterFeedService(EnterFeedRepository enterFeedRepository,
                            ArtistGroupRepository artistGroupRepository) {
        this.enterFeedRepository = enterFeedRepository;
        this.artistGroupRepository = artistGroupRepository;
    }

    /**
     * [createFeed] 엔터피드를 생성합니다.
     * @param groupName 아티스트 그룹 이름
     * @param request 요청 데이터 객체
     * @param user 요청을 수행하는 사용자
     */
    @Transactional
    public void createFeed(String groupName, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        // 아티스트 그룹 이름으로 해당 그룹을 찾습니다.
        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));

        // 아티스트 그룹과 연관된 엔터테인먼트를 찾습니다.
        Entertainment entertainment = artistGroup.getEntertainment();

        // 새로운 EnterFeed 객체를 생성합니다.
        EnterFeed enterFeed = new EnterFeed(
                entertainment,
                artistGroup,
                user,
                request.getTitle(),
                request.getContents(),
                request.getPostPicture(),
                request.getCategory(),
                request.getDate()
        );

        // 엔터피드를 저장합니다.
        enterFeedRepository.save(enterFeed);
    }

    /**
     * [getFeed] 특정 피드를 조회합니다.
     * @param groupName 아티스트 그룹 이름
     * @param feedId 피드 ID
     * @param category 피드 카테고리
     * @return EnterFeedResponseDto 조회된 피드 정보
     */
    public EnterFeedResponseDto getFeed(String groupName, Long feedId, FeedCategory category) {
        EnterFeed enterFeed = getEnterFeed(groupName, feedId, category);
        return convertToResponseDto(enterFeed);
    }

    /**
     * [getAllFeeds] 특정 그룹과 카테고리에 해당하는 모든 피드를 조회합니다.
     * @param groupName 아티스트 그룹 이름
     * @param category 피드 카테고리
     * @return List<EnterFeedResponseDto> 조회된 피드 리스트
     */
    public List<EnterFeedResponseDto> getAllFeeds(String groupName, FeedCategory category) {
        List<EnterFeed> enterFeeds = getAllEnterFeeds(groupName, category);
        return enterFeeds.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * [updateFeed] 기존 피드를 수정합니다.
     * @param groupName 아티스트 그룹 이름
     * @param feedId 피드 ID
     * @param request 수정 요청 데이터
     * @param user 수정 요청을 하는 사용자
     */
    @Transactional
    public void updateFeed(String groupName, Long feedId, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId, request.getCategory());

        // 피드의 내용을 업데이트합니다.
        enterFeed.updateContents(
                request.getTitle(),
                request.getContents(),
                request.getPostPicture(),
                request.getCategory(),
                request.getDate()
        );

        // 수정된 피드를 저장합니다.
        enterFeedRepository.save(enterFeed);
    }

    /**
     * [deleteFeed] 특정 피드를 삭제합니다.
     * @param groupName 아티스트 그룹 이름
     * @param feedId 피드 ID
     * @param user 삭제 요청을 하는 사용자
     * @param category 피드 카테고리
     */
    @Transactional
    public void deleteFeed(String groupName, Long feedId, User user, FeedCategory category) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId, category);
        enterFeedRepository.delete(enterFeed);
    }

    /**
     * [verifyEntertainmentAuthority] 사용자가 엔터테인먼트 권한을 가지고 있는지 확인합니다.
     * @param user 사용자 객체
     */
    private void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_FOUND_ENTER);
        }
    }

    /**
     * [getEnterFeed] 특정 피드를 조회합니다.
     * @param groupName 아티스트 그룹 이름
     * @param feedId 피드 ID
     * @param category 피드 카테고리
     * @return EnterFeed 조회된 피드 객체
     */
    private EnterFeed getEnterFeed(String groupName, Long feedId, FeedCategory category) {
        return enterFeedRepository.findByIdAndArtistGroupGroupNameAndCategory(feedId, groupName, category)
                .orElseThrow(() -> new CustomException(ErrorType.ENTER_FEED_NOT_FOUND));
    }

    /**
     * [getAllEnterFeeds] 특정 그룹과 카테고리에 해당하는 모든 피드를 조회합니다.
     * @param groupName 아티스트 그룹 이름
     * @param category 피드 카테고리
     * @return List<EnterFeed> 조회된 피드 리스트
     */
    private List<EnterFeed> getAllEnterFeeds(String groupName, FeedCategory category) {
        return enterFeedRepository.findAllByArtistGroupGroupNameAndCategory(groupName, category);
    }

    /**
     * [convertToResponseDto] EnterFeed 객체를 EnterFeedResponseDto로 변환합니다.
     * @param enterFeed 변환할 EnterFeed 객체
     * @return EnterFeedResponseDto 변환된 DTO 객체
     */
    private EnterFeedResponseDto convertToResponseDto(EnterFeed enterFeed) {
        return new EnterFeedResponseDto(
                enterFeed.getId(),
                enterFeed.getTitle(),
                enterFeed.getContents(),
                enterFeed.getPostPicture(),
                enterFeed.getCategory(),
                enterFeed.getDate()
        );
    }
}