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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnterFeedService {

    private final EnterFeedRepository enterFeedRepository;
    private final EntertainmentRepository entertainmentRepository;

    /**
     * [createFeed] 엔터피드를 생성합니다.
     * @param request 요청 데이터 객체
     * @param user 요청을 수행하는 사용자
     */
    @Transactional
    public void createFeed(String  enterName, EnterFeedRequestDto request, User user, ArtistGroup artistGroup) {
        Entertainment entertainment = entertainmentRepository.findByEnterName(enterName)
                .orElseThrow(() -> new CustomException(ErrorType.ARTIST_GROUP_NOT_FOUND));

        entertainment.getUser().verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = new EnterFeed(
                entertainment,
                user,
                request.getTitle(),
                request.getContents(),
                request.getCategory(),
                request.getDate(),
                artistGroup,artistGroup.getGroupName()

        );
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
     * @param category 피드 카테고리
     * @return List<EnterFeedResponseDto> 조회된 피드 리스트
     */
    public List<EnterFeedResponseDto> getAllFeeds(String enterName, FeedCategory category) {
        List<EnterFeed> enterFeeds = getAllEnterFeeds(enterName, category);
        return enterFeeds.stream()
                .map(this::convertToResponseDto)
                .toList();
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
        EnterFeed enterFeed = getEnterFeed(groupName, feedId, request.getCategory());

        enterFeed.getUser().validationUser(user);

        enterFeed.updateContents(
                request.getTitle(),
                request.getContents(),
                request.getCategory(),
                request.getDate()
        );
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
        EnterFeed enterFeed = getEnterFeed(groupName, feedId, category);

        enterFeed.getUser().validationUser(user);

        enterFeedRepository.delete(enterFeed);
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
     * @param category 피드 카테고리
     * @return List<EnterFeed> 조회된 피드 리스트
     */
    private List<EnterFeed> getAllEnterFeeds(String enterName, FeedCategory category) {
        return enterFeedRepository.findAllByEntertainmentEnterNameAndCategory(enterName, category);
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
                enterFeed.getCategory(),
                enterFeed.getScheduleDate()
        );
    }
}