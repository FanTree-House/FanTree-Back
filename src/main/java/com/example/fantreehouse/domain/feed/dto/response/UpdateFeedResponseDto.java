package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

@Getter
public class UpdateFeedResponseDto {

    private String contents;
    private String postPicture;
    //좋아요 수
    private int feedLikeCount;

    public UpdateFeedResponseDto(String contents, String postPicture, int feedLikeCount) {
        this.contents = contents;
        this.postPicture = postPicture;
        this.feedLikeCount = feedLikeCount;
    }

    public static UpdateFeedResponseDto of(Feed updatedFeed) {
        return new UpdateFeedResponseDto(
                updatedFeed.getContents(),
                updatedFeed.getPostPicture(),
                updatedFeed.getFeedLikeCount()
        );
    }
}
