package com.example.fantreehouse.domain.feedlike.service;

import com.example.fantreehouse.common.exception.errorcode.DuplicatedException;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.feedlike.repository.FeedLikeRepository;
import com.example.fantreehouse.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.fantreehouse.common.enums.ErrorType.DUPLICATE_LIKE;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedLikeServiceSupport {

    private final FeedLikeRepository feedLikeRepository;

    @Transactional
    public void addOrCancelFeedLike(User loginUser, Long feedId, Feed foundFeed) {

        try {
            FeedLike enrolledLike = loginUser.getFeedLikeList().stream()
                    .filter(feedLike -> feedLike.getFeed().getId().equals(feedId))
                    .findFirst().orElseThrow(
                            () -> new DuplicatedException(DUPLICATE_LIKE));

            cancelFeedLike(loginUser, enrolledLike);

        } catch (DuplicatedException e) {

            FeedLike feedLike = new FeedLike(foundFeed, loginUser);

            feedLikeRepository.save(feedLike);

        } catch (Exception e) {
            log.error("extra exception");
            e.printStackTrace();
        }

    }

    private void cancelFeedLike(User loginUser, FeedLike feedLike) {
        if (loginUser != null && loginUser.getFeedLikeList() != null) {
            feedLikeRepository.delete(feedLike);
        }
    }
}
