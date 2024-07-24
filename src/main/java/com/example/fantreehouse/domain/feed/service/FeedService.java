package com.example.fantreehouse.domain.feed.service;

import com.example.fantreehouse.common.exception.errorcode.AuthorizedException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.response.CreateFeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.FeedResponseDto;
import com.example.fantreehouse.domain.feed.dto.response.UpdateFeedResponseDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.common.enums.PageSize.FEED_PAGE_SIZE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final ArtistGroupRepository artistGroupRepository;


    /**
     * Feed 생성
     * @param groupName
     * @param userDetails
     * @param file
     * @param requestDto
     * @return
     */
    public CreateFeedResponseDto createFeed(String groupName, UserDetailsImpl userDetails, MultipartFile file, CreateFeedRequestDto requestDto) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        checkUserRole(loginUser.getUserRole());
        checkArtistGroup(loginUser, groupName);

        //file 경로 추출
//        if (file != null && file.isEmpty()) {
//
//            filePath = fileDirPath
//        }

//        Feed newFeed = new Feed.of(requestDto, loginUser, groupName, filePath);
        Feed newFeed = Feed.of(requestDto, loginUser, loginUser.getArtist().getArtistGroup());//file 기능 전 임시 사용

        feedRepository.save(newFeed);
        return CreateFeedResponseDto.of(newFeed);
    }

    @Transactional
    public UpdateFeedResponseDto updateFeed(String group_name, Long artistFeedId, UserDetailsImpl userDetails, MultipartFile file, UpdateFeedRequestDto requestDto) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());

        //요청하는 feed 찾기
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        checkArtistGroup(foundFeed.getUser(), group_name);
        checkWriter(loginUser.getId(), foundFeed.getUser().getId());

//        //파일 경로 추출 후, updateFeed 매개변수로 넣기 (file 은 임시)
//        String filePath = ;

//        Feed updatedFeed = foundFeed.updateFeed(requestDto, filePath);
        Feed updatedFeed = foundFeed.updateFeed(requestDto); //file 기능 전 임시 사용
        return UpdateFeedResponseDto.of(updatedFeed);
    }

    /**
     * Feed 단건 조회 - 로그인 회원 누구나
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

        checkArtistGroup(foundFeed.getUser(), groupName);
        return FeedResponseDto.of(foundFeed);
    }

    //Feed 다건 조회(페이지) - 로그인 회원 누구나
    public Page<FeedResponseDto> getAllFeed(String groupName, UserDetailsImpl userDetails,Integer page) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());

        ArtistGroup artistGroup = artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ARTISTGROUP));

        PageRequest pageRequest = PageRequest.of(page, FEED_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Feed> pagedFeed = feedRepository.findByArtistGroup(artistGroup, pageRequest);

        return pagedFeed.map(FeedResponseDto::of);
    }

    /**
     * Feed 삭제
     * @param groupName
     * @param artistFeedId
     * @param userDetails
     */
    @Transactional
    public void deleteFeed(String groupName, Long artistFeedId, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        UserRoleEnum loginUserRole = loginUser.getUserRole();

        checkUserStatus(loginUser.getStatus());

        //권한이 Artist 이거나 Entertainment 여야 함
        if (!(loginUserRole.equals(UserRoleEnum.ARTIST) ||
                loginUserRole.equals(UserRoleEnum.ENTERTAINMENT))) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
        //요청하는 feed 찾기
        Feed foundFeed = feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));

        Long loginUserId = loginUser.getId();
        Long feedWriterId = foundFeed.getUser().getId();

//        //groupName 어디에 사용하지
//        //loginUser 의 그룹이름이 groupName 과 동일한지..확인..? //엔터인 경우는 그 그룹이름을 가진 그룹을 갖는지 확인..?
//        loginUser.getArtist().getArtistGroup().getGroupName()

        // 로그인유저가 '작성자 본인'이거나 '작성자의 엔터테인먼트를 가진 유저'인 경우
        if (! (loginUserId.equals(feedWriterId) ||
                loginUser.getEntertainment().getUser().getId().equals(loginUserId))) {
            throw new AuthorizedException(UNAUTHORIZED);
        }

        feedRepository.delete(foundFeed);
    }

    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }

    //login 유저가 아티스트인지 확인
    private void checkUserRole(UserRoleEnum userRoleEnum) {
        if (!userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }
    //url 의 groupName 과 loginUser 의 소속 group 명 이 동일한지 확인
    private void checkArtistGroup(User loginUser, String groupName) {
        if (!loginUser.getArtist().getArtistGroup().getGroupName().equals(groupName)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }
    //로그인 유저와 feed 작성 유저가 동일한지 확인
    private void checkWriter(Long loginUserId, Long feedWriterId) {
        if (!loginUserId.equals(feedWriterId)) {
            throw new AuthorizedException(UNAUTHORIZED);
        }
    }
}
