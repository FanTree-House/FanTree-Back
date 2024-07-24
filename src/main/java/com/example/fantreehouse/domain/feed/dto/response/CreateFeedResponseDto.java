package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

@Getter
public class CreateFeedResponseDto {

    private String contents;
    private String post_picture;
    //좋아요 수
    private int likeCount;


    public static CreateFeedResponseDto of(Feed newFeed) {
        CreateFeedResponseDto responseDto = new CreateFeedResponseDto();
        responseDto.contents = newFeed.getContents();
        responseDto.post_picture = newFeed.getPost_picture();
        responseDto.likeCount = 0;
        return responseDto;
    }
}
