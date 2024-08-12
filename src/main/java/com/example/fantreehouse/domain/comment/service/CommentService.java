package com.example.fantreehouse.domain.comment.service;

import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.comment.dto.request.CommentRequestDto;
import com.example.fantreehouse.domain.comment.dto.response.CommentResponseDto;
import com.example.fantreehouse.domain.comment.dto.request.CreateCommentRequestDto;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.commentLike.entity.CommentLike;
import com.example.fantreehouse.domain.commentLike.repository.CommentLikeRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
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

import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.*;
import static com.example.fantreehouse.common.enums.PageSize.COMMENT_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 작성 - 해당 아티스트 & 구독한 유저가
    public void createComment(String groupName, Long feedId, UserDetailsImpl userDetails, CreateCommentRequestDto requestDto) {
        User loginUser = userDetails.getUser();
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

        Comment newComment = Comment.of(requestDto, foundFeed, loginUser);
        commentRepository.save(newComment);

    }


    // 댓글 수정 - 작성자 본인만 가능
    @Transactional
    public void updateComment(String groupName, Long feedId, Long artistFeedCommentId,
                              UserDetailsImpl userDetails, CommentRequestDto requestDto) {

        User loginUser = userDetails.getUser();
        existArtistGroup(groupName);

        Comment foundComment = checkFeedAndCommentExist(feedId, artistFeedCommentId);

        checkWriter(loginUser, foundComment.getUser());
        foundComment.update(requestDto);
    }

    //comment 삭제 - 작성자 본인, 아티스트의 경우 엔터와 어드민, 어떤 유저든 어드민 가능
    @Transactional
    public void deleteComment(String groupName, Long feedId, Long artistFeedCommentId, UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        existArtistGroup(groupName);

        Comment foundComment = checkFeedAndCommentExist(feedId, artistFeedCommentId);
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

        List<CommentResponseDto> commentLikeResponseDtoList = pageComment.getContent().stream()
                .map(comment -> {
                    List<CommentLike> commentLikeList = commentLikeRepository.findAllCommentLikeByCommentId(comment.getId());
                    int feedLikeCount = commentLikeList.size();
                    return CommentResponseDto.of(comment, feedLikeCount);
                })
                .toList();
        return new PageImpl<>(commentLikeResponseDtoList, pageRequest, pageComment.getTotalElements());
    }


    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    // 아직 존재하는 artistGroup 인지 확인 (ArtistGroup 없음은 곧 artist 게시글을 작성하는 곳이 없음을 의미)
    private void existArtistGroup(String groupName) {
        artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));
    }

    //feed 나 comment 가 이미 삭제되었는지 확인
    private Comment checkFeedAndCommentExist(Long feedId, Long artistFeedCommentId) {

        if (!feedRepository.existsById(feedId)) {
            throw new NotFoundException(FEED_NOT_FOUND);
        }

        Comment foundComment = commentRepository.findById(artistFeedCommentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT)
                );

        return foundComment;
    }

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
                Long loginUserId = loginUser.getId();
                boolean isSubscribed = subscribers.stream()
                        .anyMatch(user -> user.getId().equals(loginUserId));
                if (!isSubscribed) {
                    throw new UnAuthorizedException(NOT_SUBSCRIPT_USER);
                }
                break;

            default:
                throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    //로그인 유저가 Comment 작성자 본인
    private void checkWriter(User loginUser, User commentUser) {
        if (!loginUser.getId().equals(commentUser.getId())) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }


}

