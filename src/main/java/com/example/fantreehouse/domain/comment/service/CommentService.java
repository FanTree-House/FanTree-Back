package com.example.fantreehouse.domain.comment.service;

import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.comment.dto.CommentRequestDto;
import com.example.fantreehouse.domain.comment.dto.CommentResponseDto;
import com.example.fantreehouse.domain.comment.dto.request.CreateCommentRequestDto;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.common.enums.PageSize.COMMENT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final ArtistRepository artistRepository;

    //댓글 작성 - 해당 아티스트 & 구독한 유저가
    public void createComment(String groupName, Long feedId, UserDetailsImpl userDetails, CreateCommentRequestDto requestDto) {
        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);

        // 찾는 Feed 생사 확인
        Feed foundFeed = feedRepository.findById(feedId).orElseThrow(
                () -> new NotFoundException(FEED_NOT_FOUND)
        );

        User feedWriter = foundFeed.getUser();

        //구독자 리스트
        List<User> subscribers = foundFeed.getArtistGroup().getSubscriptionList().stream()
                .map(Subscription::getUser).toList();

        checkCommentAuthorization(loginUser, feedWriter, subscribers);

        //댓글에 저장될 이름
        //유저의 경우 nickname (닉네임)
        //아티스트의 경우 artistName (활동명)

        Comment newComment = Comment.of(requestDto, foundFeed, loginUser);
        commentRepository.save(newComment);

    }



    // 댓글 수정 - 작성자 본인만 가능
    @Transactional
    public void updateComment(String groupName, Long feedId, Long artistFeedCommentId,
                              UserDetailsImpl userDetails, CommentRequestDto requestDto) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);

        //feed 나 comment 가 이미 삭제되었는지 확인
        if (!feedRepository.existsById(feedId)) {
            throw new NotFoundException(FEED_NOT_FOUND);
        }

        Comment foundComment = commentRepository.findById(artistFeedCommentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT)
                );

        checkWriter(loginUser, foundComment.getUser());
        foundComment.update(requestDto);
    }


    //comment 삭제 //작성자 본인, 아티스트의 경우 엔터와 어드민, 어떤 유저든 어드민 가능
    public void deleteComment(String groupName, Long feedId, Long artistFeedCommentId, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);

        //feed 나 comment 가 이미 삭제되었는지 확인
        if (!feedRepository.existsById(feedId)) {
            throw new NotFoundException(FEED_NOT_FOUND);
        }

        Comment foundComment = commentRepository.findById(artistFeedCommentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT)
                );

        User commentWriter = foundComment.getUser();

        if (loginUser.getId().equals(commentWriter.getId())) {// 작성자 본인(USER 또는 ARTIST)
        } else if (loginUser.getUserRole().equals(UserRoleEnum.ADMIN)) {// ADMIN
        } else if (loginUser.getUserRole().equals(UserRoleEnum.ENTERTAINMENT)
                && commentWriter.getUserRole().equals(UserRoleEnum.ARTIST)
                && loginUser.getEntertainment().equals(commentWriter.getEntertainment())) {
        } else {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        commentRepository.delete(foundComment);
    }

    //특정 feed 의 모든 comment 조회 // activeUser 면 됨
    public Page<CommentResponseDto> getAllComment(String groupName, Long feedId, UserDetailsImpl userDetails, int page) {
        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);

        PageRequest pageRequest = PageRequest.of(page, COMMENT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> pageComment = commentRepository.findAllByFeedId(feedId, pageRequest);

        return pageComment.map(CommentResponseDto::of);
    }



    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    // 그룹 생사 확인 <- 그룹이 사라지면 게시글도 모두 사라지기 때문
    // (그룹이 생성되야 아티스트가 게시글 생성가능_즉, 댓글도 아티스트 그룹이 있어야 한다는 뜻)
    // 아직 존재하는 artistGroup 인지 확인 (ArtistGroup 없음은 곧 artist 게시글을 작성하는 곳이 없음을 의미)
    private void existArtistGroup(String groupName) {
        artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));
    }

    //comment 작성 권한 = ARTIST 이거나 USER 인 경우 (ADMIN 과 ENTERTAINMENT 제외를 위한)
//    private void canWriteComment(User loginUser) {
//        if (!loginUser.getUserRole().equals(UserRoleEnum.ARTIST)
//                || (!loginUser.getUserRole().equals(UserRoleEnum.USER)) {
//            throw new UnAuthorizedException(UNAUTHORIZED);
//        }
//    }

    // login 한 유저의 Role 에 따른 comment 작성 가능여부 확인
    private void checkCommentAuthorization(User loginUser, User feedWriter, List<User> subscribers) {
        switch (loginUser.getUserRole()) {
            case ARTIST:
                if (feedWriter.getId().equals(loginUser.getId())) {//작성자 본인인 경우

                } else {// 작성자 아닌 ARTIST 의 경우 - 구독 확인
                    if (!subscribers.contains(loginUser))
                        throw new UnAuthorizedException(NOT_SUBSCRIPT_USER);
                }
                break;

            case USER: // USER 의 경우 - 구독 확인
                if (!subscribers.contains(loginUser))
                    throw new UnAuthorizedException(NOT_SUBSCRIPT_USER);
                break;

            default:
                throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

//    //로그인 유저가 feed 작성자 본인(ARTIST) 이거나, 구독자 중 USER 또는 ARTIST 인 유저
//    private void canWriteComment(User loginUser, User feedUser, List<User> subscribers) {
//        if (!loginUser.getId().equals(feedUser.getId()))
//            User.hasCommentAuthorization(loginUser, subscribers);
//    }

    //로그인 유저가 Comment 작성자 본인
    private void checkWriter(User loginUser, User commentUser) {
        if (!loginUser.getId().equals(commentUser.getId())) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }


}

