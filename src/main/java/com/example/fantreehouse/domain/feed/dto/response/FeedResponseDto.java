package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class FeedResponseDto {

    private String contents;
    private List<String> imageUrls;
    private int likesCount;

    public FeedResponseDto(String contents, List<String> imageUrls, int likesCount) {
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.likesCount = likesCount;
    }

    public static FeedResponseDto of(Feed feed, int feedLikeCount, List<String> imageUrls) {
        return new FeedResponseDto(
                feed.getContents(),
                imageUrls,
                feedLikeCount
        );

    }
}
