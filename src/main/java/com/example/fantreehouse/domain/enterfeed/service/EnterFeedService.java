package com.example.fantreehouse.domain.enterfeed.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedRequestDto;
import com.example.fantreehouse.domain.enterfeed.dto.EnterFeedResponseDto;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.enterfeed.repository.EnterFeedRepository;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.entertainment.repository.EntertainmentRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnterFeedService {

    private final EnterFeedRepository enterFeedRepository;
    private final EntertainmentRepository entertainmentRepository;

    @Autowired
    public EnterFeedService(EnterFeedRepository enterFeedRepository, EntertainmentRepository entertainmentRepository) {
        this.enterFeedRepository = enterFeedRepository;
        this.entertainmentRepository = entertainmentRepository;
    }

    /**
     * [createNotice] 공지사항 생성
     * @param groupName 그룹 이름
     * @param request 요청 객체
     * @param user 로그인한 사용자 정보
     */
    @Transactional
    public void createNotice(String groupName, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        Entertainment entertainment = entertainmentRepository.findByEnterName(groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ENTERTAINMENT_NOT_FOUND));

        EnterFeed enterFeed = new EnterFeed(
                request.getFeedId(),
                entertainment,
                null,
                user,
                request.getContents(),
                request.getPostPicture(),
                "NOTICE",
                request.getDate());

        enterFeedRepository.save(enterFeed);
    }

    /**
     * [getNotice] 특정 공지사항 조회
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @return 공지사항 응답 DTO
     */
    public EnterFeedResponseDto getNotice(String groupName, String feedId) {
        EnterFeed enterFeed = getEnterFeed(groupName, feedId);
        return convertToResponseDto(enterFeed);
    }

    /**
     * [getAllNotices] 모든 공지사항 조회
     * @param groupName 그룹 이름
     * @return 공지사항 응답 DTO 리스트
     */
    public List<EnterFeedResponseDto> getAllNotices(String groupName) {
        List<EnterFeed> enterFeeds = getAllEnterFeeds(groupName, "NOTICE");
        return enterFeeds.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * [updateNotice] 공지사항 수정
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @param request 요청 객체
     * @param user 로그인한 사용자 정보
     */
    @Transactional
    public void updateNotice(String groupName, String feedId, EnterFeedRequestDto request, User user) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId);
        enterFeed.setContents(request.getContents());
        enterFeed.setPostPicture(request.getPostPicture());
        enterFeed.setCategory("NOTICE");
        enterFeed.setDate(request.getDate());

        enterFeedRepository.save(enterFeed);
    }

    /**
     * [deleteNotice] 공지사항 삭제
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @param user 로그인한 사용자 정보
     */
    @Transactional
    public void deleteNotice(String groupName, String feedId, User user) {
        verifyEntertainmentAuthority(user);

        EnterFeed enterFeed = getEnterFeed(groupName, feedId);
        enterFeedRepository.delete(enterFeed);
    }

    /**
     * [verifyEntertainmentAuthority] 엔터테인먼트 권한 확인
     * @param user 로그인한 사용자 정보
     */
    private void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_FOUND_ENTER);
        }
    }

    /**
     * [getEnterFeed] 엔터피드 조회
     * @param groupName 그룹 이름
     * @param feedId 피드 ID
     * @return 엔터피드
     */
    private EnterFeed getEnterFeed(String groupName, String feedId) {
        return enterFeedRepository.findByFeedIdAndEntertainmentEnterName(feedId, groupName)
                .orElseThrow(() -> new CustomException(ErrorType.ENTER_FEED_NOT_FOUND));
    }

    /**
     * [getAllEnterFeeds] 모든 엔터피드 조회
     * @param groupName 그룹 이름
     * @param category 카테고리
     * @return 엔터피드 리스트
     */
    private List<EnterFeed> getAllEnterFeeds(String groupName, String category) {
        return enterFeedRepository.findAllByEntertainmentEnterNameAndCategory(groupName, category);
    }

    /**
     * [convertToResponseDto] 엔터피드를 응답 DTO로 변환
     * @param enterFeed 엔터피드
     * @return 엔터피드 응답 DTO
     */
    private EnterFeedResponseDto convertToResponseDto(EnterFeed enterFeed) {
        return new EnterFeedResponseDto(
                enterFeed.getFeedId(),
                enterFeed.getContents(),
                enterFeed.getPostPicture(),
                enterFeed.getCategory(),
                enterFeed.getDate()
        );
    }
}