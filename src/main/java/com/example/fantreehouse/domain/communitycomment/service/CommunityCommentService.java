package com.example.fantreehouse.domain.communitycomment.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.artistgroup.repository.ArtistGroupRepository;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentResponseDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentUpdateRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentUpdateResponseDto;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communitycomment.repository.CommunityCommentRepository;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.communityfeed.repository.CommunityFeedRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityFeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ArtistGroupRepository artistGroupRepository;


    //댓글생성
    public CommunityCommentResponseDto createComment(CommunityCommentRequestDto requestDto, Long userId, Long feedId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
        CommunityFeed feed = findFeed(feedId);

        CommunityComment comment = new CommunityComment(requestDto, user);
        commentRepository.save(comment);

        return new CommunityCommentResponseDto(comment);
    }

    //댓글 전체조회
    public List<CommunityCommentResponseDto> findAllComment(String groupName, Long feedId) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityFeed feed = findFeed(feedId);
        List<CommunityComment> commentList = commentRepository.findAllByCommunityFeed_Id(feedId);
        if (commentList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_COMMENT);
        }

        return commentList.stream()
                .map(CommunityCommentResponseDto::new)
                .toList();
    }

    //댓글수정

    public CommunityComment updateComment(Long commentId, CommunityCommentUpdateRequestDto requestDto, Long userId,
                                                           String groupName) {
        User user = findUser(userId);
        ArtistGroup artistGroup = findArtistGroup(groupName);
        CommunityComment comment = findComment(commentId);
        // Todo : User 랑 CommunityFeed 가 널로들어와서 리팩토링해야함

        if (!comment.getNickname().equals(user.getNickname())) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
        }
        if (comment.getContents().equals(requestDto.getContents())) {
            throw new CustomException(ErrorType.DUPLICATE_COMMENT);
        }
        comment.updateComment(requestDto, user);
        return comment;
    }

    //댓글삭제
    public void deleteComment(Long commentId, Long userId , String groupName) {
        ArtistGroup artistGroup = findArtistGroup(groupName);
        User user = findUser(userId);
        CommunityComment comment = findComment(commentId);

        if (!comment.getNickname().equals(user.getNickname())) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
            // Todo : User 랑 CommunityFeed 가 널로들어와서 리팩토링해야함

        }
        commentRepository.delete(comment);
    }



    //유저조회
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));
    }
    //아티스트 그룹 조회
    public ArtistGroup findArtistGroup(String groupName) {
        return artistGroupRepository.findByGroupName(groupName).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_ARTISTGROUP));
    }
    //피드조회
    public CommunityFeed findFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));
    }

    //댓글찾기
    public CommunityComment findComment(Long commentId) {
        return commentRepository.findAllById(commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_COMMENT));
    }
}


