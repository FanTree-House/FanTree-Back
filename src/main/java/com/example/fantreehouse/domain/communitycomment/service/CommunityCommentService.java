package com.example.fantreehouse.domain.communitycomment.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.comment.repository.CommentRepository;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentResponseDto;
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

public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityFeedRepository feedRepository;
    private final UserRepository userRepository;

    //댓글생성
    public CommunityCommentResponseDto createcComment(CommunityCommentRequestDto requestDto, User user, Long community_feed_id) {
        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        CommunityFeed feed = feedRepository.findById(community_feed_id).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUND_FEED));

        if (!feed.getUser().getArtist().getArtistGroup().getId().equals(user.getArtist().getArtistGroup().getId())) {
            throw new CustomException(ErrorType.NOT_MATCH_USER);
        }
        CommunityComment comment = new CommunityComment(requestDto);
        commentRepository.save(comment);

        return new CommunityCommentResponseDto(comment);
    }

    //댓글수정
    public CommunityComment updateComment(Long commentId,
                                          CommunityCommentRequestDto requestDto,
                                          User user) {

        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        CommunityComment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUNT_COMMENT));
        if (!comment.getCommunityFeed().getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
//            Todo : 테스트가능할때 키값이 제대로 매치가 되었는지 확인해야함
           }
        comment.updateComment(requestDto);
        return comment;
    }

    //댓글삭제
    public void deleteComment(Long commentId, User user) {
        CommunityComment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new CustomException(ErrorType.NOT_FOUNT_COMMENT));
        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

        if (!comment.getCommunityFeed().getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_USER_COMMENT);
            //            Todo : 테스트가능할때 키값이 제대로 매치가 되었는지 확인해야함

        }
        commentRepository.delete(comment);
    }

    //댓글 전체조회
    public List<CommunityCommentResponseDto> findAllComment(User user) {
        user = userRepository.findById(user.getId()).orElseThrow(()
                -> new CustomException(ErrorType.USER_NOT_FOUND));

//        Todo? : 유저가 구독한사람인지 인증해야하는 과정이 필요한지 고민하기

        List<CommunityComment> commentList = commentRepository.findAll();
        if (commentList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUNT_COMMENT);

        }
        return commentList.stream()
                .map(CommunityCommentResponseDto::new)
                .toList();
    }
}


