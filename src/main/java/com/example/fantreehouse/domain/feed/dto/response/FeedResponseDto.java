package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

@Getter
public class FeedResponseDto {

    private String contents;
    private String postPicture;

    private int likesCount;

    public FeedResponseDto(String contents, String postPicture, int likesCount) {
        this.contents = contents;
        this.postPicture = postPicture;
        this.likesCount = likesCount;
    }

    public static FeedResponseDto of(Feed feed, int feedLikeCount) {
        return new FeedResponseDto(
                feed.getContents(),
                feed.getPostPicture(),
                feedLikeCount
        );

    }
}
