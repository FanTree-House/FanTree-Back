package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

@Getter
public class FeedResponseDto {

    private String contents;
    private String postPicture;

    //좋아요 수
    private int likesCount;

    public FeedResponseDto(String contents, String postPicture, int likesCount) {
        this.contents = contents;
        this.postPicture = postPicture;
        this.likesCount = likesCount;
    }

    public static FeedResponseDto of(Feed feed) {
        return new FeedResponseDto(
                feed.getContents(),
                feed.getPostPicture(),
                feed.getLikesCount()
        );

    }
}
