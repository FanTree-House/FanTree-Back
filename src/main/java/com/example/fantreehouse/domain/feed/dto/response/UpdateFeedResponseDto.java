package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateFeedResponseDto {

    private String contents;
    private List<String> imageUrls;
    private int feedLikeCount;

    public UpdateFeedResponseDto(String contents, List<String> imageUrls, int feedLikeCount) {
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.feedLikeCount = feedLikeCount;
    }

    public static UpdateFeedResponseDto of(Feed updatedFeed) {
        return new UpdateFeedResponseDto(
                updatedFeed.getContents(),
                updatedFeed.getImageUrls(),
                updatedFeed.getFeedLikeCount()
        );
    }
}
