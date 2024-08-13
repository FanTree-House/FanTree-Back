package com.example.fantreehouse.domain.communityLike.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communityLike.repository.CommunityLikeRepository;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CommunityLikeService {
    private final CommunityLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommunityFeedRepository feedRepository;
    private final ArtistGroupRepository artistGroupRepository;
    private final CommunityCommentRepository commentRepository;

    //피드 좋아요
    public CommunityLike pressFeedLike(Long userId, Long feedId, String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        User user = findUser(userId);
        CommunityFeed feed = findFeed(feedId);

        if (likeRepository.findByUserIdAndCommunityFeedId(userId, feedId).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }
        CommunityLike feedLike = new CommunityLike(user, feed);
        feed.pressFeedLike(user, feed);
        likeRepository.save(feedLike);
        return feedLike;
    }

    //피드 좋아요 취소
    public void pressFeedIsLike(Long userId, Long feedId, String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        User user = findUser(userId);
        CommunityFeed feed = findFeed(feedId);
        CommunityLike feedLike = likeRepository.findByUserIdAndCommunityFeedId(userId, feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED_LIKE));

        feed.pressFeedIsLike(user, feed);
        likeRepository.delete(feedLike);
    }

    //커뮤 피드 좋아요 취소 (프론트 적용)
    public void cancelFeedLike(Long id, Long communityFeedId) {
        User user = findUser(id);
        CommunityFeed feed = findFeed(communityFeedId);
        CommunityLike feedLike = likeRepository.findByUserIdAndCommunityFeedId(id, communityFeedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED_LIKE));

        feed.pressFeedIsLike(user, feed);
        likeRepository.delete(feedLike);
    }

    // 댓글 좋아요기능
    public void pressCommentLike(Long userId, Long feedId, String groupName, Long commentId) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);
        CommunityComment comment = findComment(commentId);

        if (likeRepository.findByUserIdAndCommunityCommentId(userId, feedId).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_LIKE);
        }

        CommunityLike commentLike = new CommunityLike(user, comment);
        comment.pressCommentLike(user, comment, feed);
        likeRepository.save(commentLike);
    }

    //피드 댓글 좋아요 취소기능
    public void pressCommentIsLike(Long userId, Long commentId, String groupName, Long feedId) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        User user = findUser(userId);
        CommunityFeed feed = findFeed(feedId);

        CommunityComment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_COMMENT));

        CommunityLike feedLike = likeRepository.findByUserIdAndCommunityCommentId(userId, commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_COMMENT_LIKE));

        comment.pressCommentIsLike(user, comment, feed);
        likeRepository.delete(feedLike);
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

    // 댓글 찾기
    public CommunityComment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_USER_FEED));
    }

    //아티스트 그룹찾기
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }


}
