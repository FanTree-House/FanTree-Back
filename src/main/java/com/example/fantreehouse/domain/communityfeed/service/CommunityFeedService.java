package com.example.fantreehouse.domain.communityfeed.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.areFilesExist;

@Service
@RequiredArgsConstructor

public class CommunityFeedService {

    private final CommunityFeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CommunityLikeRepository likeRepository;
    private final S3FileUploader s3FileUploader;

    @Transactional //피드생성
    public CommunityFeedResponseDto createFeed(
        CommunityFeedRequestDto requestDto,
        List<MultipartFile> files,
        Long userId,
        String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        checkUserStatus(user.getStatus(), user.getUserRole());

        // 구독자 체크
        checksubscriptionList(userId);

        CommunityFeed feed = new CommunityFeed(requestDto, user, artistGroup);
        feedRepository.save(feed);

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) {
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveCommunityImage(file, groupName, feed.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(feed.getId(), imageUrls);
        updateCommunityFeedImageUrls(carrier);

        return new CommunityFeedResponseDto(feed);
    }

    private void updateCommunityFeedImageUrls(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrls().isEmpty()) {
            CommunityFeed feed = feedRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
            feed.updateImageUrls(carrier.getImageUrls());
            feedRepository.save(feed);
        }
    }

    //    피드 전체 조회
    public List<CommunityFeedResponseDto> findAllFeed(Long userId, String gruopName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(gruopName);


        // 구독자 체크
        checksubscriptionList(userId);
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
        ArtistGroup artistGroup = findArtistGroup(groupName);
        User user = findUser(userId);
        CommunityFeed feed = feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));

        // 구독자 체크
        checksubscriptionList(userId);

        return feed;
    }

    //피드 업데이트
    @Transactional
    public void updateFeed(CommunityFeedUpdateRequestDto requestDto, List<MultipartFile> files, Long feedId, Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_FEED);
        }
        feed.updateFeed(requestDto);

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) {
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveCommunityImage(file, groupName, feed.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }
        ImageUrlCarrier carrier = new ImageUrlCarrier(feed.getId(), imageUrls);
        updateCommunityFeedImageUrls(carrier);
    }

    // 피드 삭제 - 작성자와 UserRole이 엔터와 어드민이면 삭제가능
    @Transactional
    public void deleteFeed(Long feedId, Long userId, String groupName, UserRoleEnum userRoleEnum) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);
        if (!(feed.getUser().getId().equals(user.getId()) ||
                userRoleEnum.equals(UserRoleEnum.ADMIN) ||
                userRoleEnum.equals(UserRoleEnum.ENTERTAINMENT))) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_DELETE);
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
                -> new CustomException(ErrorType.NOT_USER_FEED));
    }

    //아티스트 그룹찾기
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }

    //유저가 좋아요를 눌렀나 확인
    public CommunityLike checkUserLike(Long userId) {
        return likeRepository.findByUserId(userId).orElseThrow(()
                -> new CustomException(ErrorType.DUPLICATE_LIKE));
    }

    //휴면유저와 UserROle 필터링
    private void checkUserStatus(UserStatusEnum userStatus, UserRoleEnum userRoleEnum) {
        if (!(userStatus.equals(UserStatusEnum.ACTIVE_USER) && userRoleEnum.equals(userRoleEnum.USER))) {
            throw new CustomException(ErrorType.UNAUTHORIZED_FEED_CREATE);
        }
    }

    //구독자 체크
    private void checksubscriptionList(Long userId){
        List<Subscription> subscriptionList = subscriptionRepository
            .findAllByUserId(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
        for (Subscription subscription : subscriptionList) {
            if (!subscription.getUser().getId().equals(userId)) {
                throw new CustomException(ErrorType.UNAUTHORIZED_FEED_ACCESS);
            }
        }
    }


}

