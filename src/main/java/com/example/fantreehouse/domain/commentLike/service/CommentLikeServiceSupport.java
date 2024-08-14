package com.example.fantreehouse.domain.commentLike.service;

import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.commentLike.entity.CommentLike;
import com.example.fantreehouse.domain.commentLike.repository.CommentLikeRepository;
import com.example.fantreehouse.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.fantreehouse.common.enums.ErrorType.DUPLICATE_LIKE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeServiceSupport {

    private final CommentLikeRepository commentLikeRepository;

    public void addOrCancelFeedLike(User loginUser, Long commentId, Comment foundComment) {
        try {
            CommentLike enrolledLike = loginUser.getCommentLikeList().stream()
                    .filter(commentLike -> commentLike.getComment().getId().equals(commentId))
                    .findFirst().orElseThrow(
                            () -> new DuplicatedException(DUPLICATE_LIKE));

            cancelFeedLike(loginUser, enrolledLike);

        } catch (DuplicatedException e) {

            CommentLike commentLike = new CommentLike(foundComment, loginUser);

            commentLikeRepository.save(commentLike);

        } catch (Exception e) {
            log.error("extra exception");
            e.printStackTrace();
        }
    }

    // 좋아요 유무
    public Boolean isLiked(User loginUser, Long artistFeedCommentId) {
        return commentLikeRepository.findByUserIdAndCommentId(loginUser.getId(), artistFeedCommentId).isPresent();
    }

    private void cancelFeedLike(User loginUser, CommentLike commentLike) {
        if (loginUser != null && loginUser.getFeedLikeList() != null) {
            commentLikeRepository.delete(commentLike);
        }
    }

    // 좋아요 개수
    public Long likeCount(Long artistFeedCommentId) {
        return commentLikeRepository.countByCommentId(artistFeedCommentId);
    }
}
