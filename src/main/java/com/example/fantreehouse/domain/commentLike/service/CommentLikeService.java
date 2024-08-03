package com.example.fantreehouse.domain.commentLike.service;

import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.feed.repository.FeedRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import com.example.fantreehouse.domain.user.entity.UserStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fantreehouse.common.enums.ErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final ArtistGroupRepository artistGroupRepository;
    private final FeedRepository feedRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeServiceSupport commentLikeServiceSupport;

    @Transactional
    public void addOrCancelLike(String groupName, Long artistFeedId, Long artistFeedCommentId, UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        checkUserStatus(loginUser.getStatus());
        existArtistGroup(groupName);
        existFeed(artistFeedId);

        Comment foundComment = commentRepository.findById(artistFeedCommentId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT));

        User commentWriter = foundComment.getUser();
        if (loginUser.getId().equals(commentWriter.getId())) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        //ADMIN 과 ENTER 도 좋아요를 누를 수 있는지 확인 필요 <- 누를 수 없을 경우 필터링 하는 메서드
        if(loginUser.getUserRole().equals(UserRoleEnum.ADMIN)
                || (loginUser.getUserRole().equals(UserRoleEnum.ENTERTAINMENT))){
            throw new UnAuthorizedException(UNAUTHORIZED);
        }

        commentLikeServiceSupport.addOrCancelFeedLike(loginUser, artistFeedCommentId, foundComment);
    }

    //유저 status 확인 (활동 여부)
    private void checkUserStatus(UserStatusEnum userStatusEnum) {
        if (!userStatusEnum.equals(UserStatusEnum.ACTIVE_USER)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

    private void existArtistGroup(String groupName) {
        artistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new NotFoundException(ARTIST_GROUP_NOT_FOUND));
    }

    private void existFeed(Long artistFeedId) {
        feedRepository.findById(artistFeedId)
                .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
    }
}
