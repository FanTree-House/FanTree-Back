package com.example.fantreehouse.domain.communityfeed.service;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedResponseDtoExtension;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.subscription.repository.SubscriptionRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.areFilesExist;

@Slf4j
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

        // 구독자 체크
        checkSubscriptionList(userId,artistGroup.getId());

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
                s3FileUploader.deleteFilesInBucket(imageUrls);
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(feed.getId(), imageUrls);
        updateCommunityFeedImageUrls(carrier);

        return new CommunityFeedResponseDto(feed);
    }

    //    피드 전체 조회
    public List<CommunityFeedResponseDto> findAllFeed(Long userId, String gruopName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(gruopName);


        // 구독자 체크
        checkSubscriptionList(userId, artistGroup.getId());
        List<CommunityFeed> feedList = feedRepository.findAll();

        if (feedList.isEmpty()) {
            throw new CustomException(NOT_FOUND_FEED);
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
                -> new CustomException(NOT_FOUND_FEED));

        // 구독자 체크
        checkSubscriptionList(userId, artistGroup.getId());

        return feed;
    }

    // 개인별 피드 전체 조회
    public List<CommunityFeedResponseDto> findAllMyFeeds(User user) {

        List<Subscription> subscriptionList = user.getSubscriptions();

        List<CommunityFeed> allMyFeeds = new ArrayList<>();
        //유저의 구독그룹리스트의 커뮤니티에서 Feed 들을 뽑아온다
        for (Subscription subscription : subscriptionList) {
            try {
                List<CommunityFeed> feedList = feedRepository.findAllByArtistGroupId(subscription.getArtistGroup().getId())
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_FEED));
                allMyFeeds.addAll(feedList);
            } catch (NotFoundException e) {
                log.info(subscription.getArtistGroup() + "에 사용자로 등록된 커뮤니티 피드가 없습니다.");
            } catch (Exception e) {
                log.error("커뮤니티 피드를 검색 중 오류 발생");
            }
        }

        if (allMyFeeds.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_FEED); //이 경우 프론트에서 받아서 메세지 전달 가능
        }

        return allMyFeeds
                .stream().sorted(Comparator.comparing(CommunityFeed::getCreatedAt)
                .reversed()).map(CommunityFeedResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<CommunityFeedResponseDtoExtension> findAllLikeFeeds(User user) {

        //좋아요 누른 feed 찾기
        List<CommunityLike> communityLikeList = likeRepository.findAllByUserId(user.getId());
        if (communityLikeList.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_FEED_LIKES);
        }

        List<CommunityFeed> foundFeedList = communityLikeList.stream().map(CommunityLike::getCommunityFeed)
                .sorted(Comparator.comparing(CommunityFeed::getCreatedAt).reversed())
                .toList();

        List<CommunityFeedResponseDtoExtension> feedResponseDtoList = new ArrayList<>();

        for (CommunityFeed feed : foundFeedList) {
            Long likeCount = likeRepository.countByCommunityFeedId(feed.getId());
            feedResponseDtoList.add(CommunityFeedResponseDtoExtension.of(feed, likeCount));
        }
        return feedResponseDtoList;
    }

    //피드 업데이트
    @Transactional
    public void updateFeed(CommunityFeedUpdateRequestDto requestDto, List<MultipartFile> files, Long feedId, Long userId, String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(NOT_USER_FEED);
        }
        feed.updateFeed(requestDto);

        if (areFilesExist(files)) {
            List<String> foundFeedImageUrls = feed.getImageUrls();
            for (String imageUrl : foundFeedImageUrls) {
                try {
                    s3FileUploader.deleteFileInBucket(imageUrl);
                } catch (NotFoundException e) {
                    foundFeedImageUrls.remove(imageUrl);
                    feed.updateImageUrls(foundFeedImageUrls);
                    feedRepository.save(feed);
                } catch (Exception e) {
                    throw new S3Exception(DELETE_ERROR);
                }
            }

            List<String> imageUrls = new ArrayList<>();
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveCommunityImage(file, groupName, feed.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                s3FileUploader.deleteFilesInBucket(imageUrls);
                throw new S3Exception(UPLOAD_ERROR);
            }
            ImageUrlCarrier carrier = new ImageUrlCarrier(feed.getId(), imageUrls);
            updateCommunityFeedImageUrls(carrier);
        }
    }


    private void updateCommunityFeedImageUrls(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrls().isEmpty()) {
            CommunityFeed feed = feedRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
            feed.updateImageUrls(carrier.getImageUrls());
            feedRepository.save(feed);

        }
    }

    // 피드 삭제 - 작성자와 UserRole이 엔터와 어드민이면 삭제가능
    @Transactional
    public void deleteFeed(Long feedId, Long userId, String groupName, UserRoleEnum userRoleEnum) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);

        user.validationEnterAndAdmin(findUser(userId));

        List<String> foundFeedImageUrls = feed.getImageUrls();
        for (String imageUrl : foundFeedImageUrls) {
            try {
                s3FileUploader.deleteFileInBucket(imageUrl);
            } catch (NotFoundException e) {
                foundFeedImageUrls.remove(imageUrl);
                feed.updateImageUrls(foundFeedImageUrls);//실체 없는 url 테이블에서 삭제
                feedRepository.save(feed);
            } catch (Exception e) {
                throw new S3Exception(DELETE_ERROR);
            }
            feedRepository.delete(feed);
        }
    }

    //유저찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(USER_NOT_FOUND));
    }

    //피드찾기
    public CommunityFeed findFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(NOT_USER_FEED));
    }

    //아티스트 그룹찾기
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(NOT_FOUND_ARTISTGROUP));
    }

    //유저가 좋아요를 눌렀나 확인
    public CommunityLike checkUserLike(Long userId) {
        return likeRepository.findByUserId(userId).orElseThrow(()
                -> new CustomException(DUPLICATE_LIKE));
    }

    //구독자 체크
    private void checkSubscriptionList(Long userId, Long artistGroupId) {
        List<Subscription> subscriptionList = subscriptionRepository
                .findAllByUserId(userId).orElseThrow(()
                        -> new CustomException(USER_NOT_FOUND));
        boolean isSubscribed = false;
        for (Subscription subscription : subscriptionList) {
            if (!subscription.getUser().getId().equals(userId)) {
                throw new CustomException(UNAUTHORIZED_FEED_ACCESS);
            }

            // 구독한 아티스트인지 확인
            if (subscription.getArtistGroup().getId().equals(artistGroupId)) {
                isSubscribed = true;
                break;
            }
        }

        // 구독하지 않은 아티스트인 경우 예외 발생
        if (!isSubscribed) {
            throw new CustomException(ARTIST_NOT_SUBSCRIBED);
        }
    }
}

