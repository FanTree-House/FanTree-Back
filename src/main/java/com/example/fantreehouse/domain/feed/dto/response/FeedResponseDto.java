package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Builder;

public class FeedResponseDto {

    private String contents;
    private String post_picture;

    //좋아요 수
    private int likesCount;

    public static FeedResponseDto of(Feed feed) {
        FeedResponseDto responseDto = new FeedResponseDto();
        responseDto.contents = feed.getContents();
        responseDto.post_picture = feed.getPost_picture();
//        responseDto.likesCount = feed.getLikesCount();
        return responseDto;

    }
}
