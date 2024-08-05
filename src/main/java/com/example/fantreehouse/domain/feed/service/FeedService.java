package com.example.fantreehouse.domain.feed.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.response.CreateFeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.UpdateFeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.common.enums.PageSize.FEED_PAGE_SIZE;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.areFilesExist;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final ArtistRepository artistRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final S3FileUploader s3FileUploader;
    private final AmazonS3Client amazonS3Client;


    /**
     * Feed 생성
     *
     * @param groupName
     * @param userDetails
     * @param files
     * @param requestDto
     * @return
     */
    @Transactional
    public CreateFeedResponseDto createFeed(String groupName, UserDetailsImpl userDetails,
                                            List<MultipartFile> files, CreateFeedRequestDto requestDto) {

        User loginUser = userDetails.getUser(); //로그인 유저
        checkUserStatus(loginUser.getStatus()); // 활성유저인지 확인
        checkUserRole(loginUser.getUserRole()); //Artist 권한 확인

        //로그인한 아티스트가 groupName 이라는 이름을 가진 ArtistGroup 소속인지 확인 <- 아티스트 그룹을 통해서 아티스트를 찾아 올 수 없는 구조이므로
        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));

        Artist loginArtist = checkLoginUserRole(loginUser.getId());

        if (!artistGroup.getArtists().contains(loginArtist)) {
            throw new NotFoundException(ARTIST_NOT_FOUND);
        }

        Feed newFeed = Feed.of(requestDto, loginUser, artistGroup);
        feedRepository.save(newFeed);

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) {
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveArtistFeedImage(file, loginArtist.getArtistName(), newFeed.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(newFeed.getId(), imageUrls);
        updateFeedImageUrls(carrier);

        return CreateFeedResponseDto.of(newFeed);
    }

    /**
     * Feed 수정
     *
     * @param groupName
     * @param artistFeedId
     * @param userDetails
     * @param requestDto
     * @return
     */
    @Transactional
    public UpdateFeedResponseDto updateFeed(String groupName, Long artistFeedId, UserDetailsImpl userDetails,
                                            List<MultipartFile> files, UpdateFeedRequestDto requestDto) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());

        //요청하는 feed 찾기
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        Artist loginArtist = checkLoginUserRole(loginUser.getId());

        checkArtistGroup(loginArtist, groupName);
        checkWriter(loginUser.getId(), foundFeed.getUser().getId());

        foundFeed.updateFeed(requestDto);

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) {
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveArtistFeedImage(file, loginArtist.getArtistName(), foundFeed.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(foundFeed.getId(), imageUrls);
        updateFeedImageUrls(carrier);

        return UpdateFeedResponseDto.of(foundFeed);
    }


    /**
     * Feed 단건 조회 - 로그인 회원 누구나
     *
     * @param groupName
     * @param artistFeedId
     * @param userDetails
     * @return
     */
    public FeedResponseDto getFeed(String groupName, Long artistFeedId, UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());

        //요청하는 feed 찾기
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        List<FeedLike> feedLikeList = feedLikeRepository.findAllFeedLikeByFeedId(artistFeedId);
        int feedLikeCount = feedLikeList.size();

        List<String> imageUrls = new ArrayList<>();
        for (String imageUrl : foundFeed.getImageUrls()) {
            String url = s3FileUploader.getFileUrl(imageUrl);//이미지 url 가져옴
            imageUrls.add(url);
        }

        return FeedResponseDto.of(foundFeed, feedLikeCount, imageUrls, foundFeed.getId(), foundFeed.getArtistName());
    }


    //Feed 다건 조회(페이지) - 로그인 회원 누구나
    public Page<FeedResponseDto> getAllFeed(String groupName, Integer page) {

        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTISTGROUP));

        PageRequest pageRequest = PageRequest.of(page, FEED_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Feed> pagedFeed = feedRepository.findByArtistGroup(artistGroup, pageRequest);

        List<FeedResponseDto> feedLikeResponseDtoList = pagedFeed.getContent().stream()
                .map(feed -> {
                    List<FeedLike> feedLikeList = feedLikeRepository.findAllFeedLikeByFeedId(feed.getId());
                    int feedLikeCount = feedLikeList.size();

                    return FeedResponseDto.of(feed, feedLikeCount);
                })
                .toList();

        return new PageImpl<>(feedLikeResponseDtoList, pageRequest, pagedFeed.getTotalElements());
    }

    //Feed 삭제
    @Transactional
    public void deleteFeed(Long artistFeedId, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        UserRoleEnum loginUserRole = loginUser.getUserRole();
        checkUserStatus(loginUser.getStatus());

        //권한이 Artist 이거나 Entertainment 여야 함
        if (!(loginUserRole.equals(UserRoleEnum.ARTIST) ||
                loginUserRole.equals(UserRoleEnum.ENTERTAINMENT))) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
        //요청하는 feed 찾기
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        Long loginUserId = loginUser.getId();
        Long feedWriterId = foundFeed.getUser().getId();

        // 로그인유저가 '작성자 본인'이거나 '작성자의 엔터테인먼트를 가진 유저'인 경우
        if (!(loginUserId.equals(feedWriterId) ||
                loginUser.getEntertainment().getUser().getId().equals(loginUserId))) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        s3FileUploader.deleteFilesInBucket(foundFeed.getImageUrls());
        feedRepository.delete(foundFeed);

    }


    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    //login 유저가 아티스트인지 확인
    private void checkUserRole(UserRoleEnum userRoleEnum) {
        if (!userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    //요청하는 feed 찾기
    private Feed findFeed(Long artistFeedId) {
        return feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
    }

    private void updateFeedImageUrls(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrls().isEmpty()) {
            Feed feed = feedRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
            feed.updateImageUrls(carrier.getImageUrls());
            feedRepository.save(feed);
        }
    }

    //url 의 groupName 과 loginUser 의 소속 group 명이 동일한지 확인
    private void checkArtistGroup(Artist loginArtist, String groupName) {
        if (!loginArtist.getArtistGroup().getGroupName().equals(groupName)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    private Artist checkLoginUserRole(Long userId) {
        return artistRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ARTIST_NOT_FOUND));
    }

    //로그인 유저와 feed 작성 유저가 동일한지 확인
    private void checkWriter(Long loginUserId, Long feedWriterId) {
        if (!loginUserId.equals(feedWriterId)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }
}
