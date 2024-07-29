package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

@Getter
public class UpdateFeedResponseDto {

    private String contents;
    private String postPicture;
    //좋아요 수
    private int likeCount;

    public UpdateFeedResponseDto(String contents, String postPicture, int likeCount) {
        this.contents = contents;
        this.postPicture = postPicture;
        this.likeCount = likeCount;
    }

    public static UpdateFeedResponseDto of(Feed updatedFeed) {
        return new UpdateFeedResponseDto(
                updatedFeed.getContents(),
                updatedFeed.getPostPicture(),
                updatedFeed.getLikesCount()
        );
    }
}
