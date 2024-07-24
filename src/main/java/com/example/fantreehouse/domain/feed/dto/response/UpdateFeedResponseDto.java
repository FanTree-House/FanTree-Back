package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;

public class UpdateFeedResponseDto {

    private String contents;
    private String post_picture;
    //좋아요 수
    private int likeCount;

    public static UpdateFeedResponseDto of(Feed updatedFeed) {
        UpdateFeedResponseDto responseDto = new UpdateFeedResponseDto();
        responseDto.contents = updatedFeed.getContents();
        responseDto.post_picture = updatedFeed.getPost_picture();
        return responseDto;
    }
}
