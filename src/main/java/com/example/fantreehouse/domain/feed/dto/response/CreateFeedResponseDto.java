package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateFeedResponseDto {

    private String contents;
    //좋아요 수
    private int likeCount;

    private List<String> imageUrls;


    public static CreateFeedResponseDto of(Feed newFeed) {
        CreateFeedResponseDto responseDto = new CreateFeedResponseDto();
        responseDto.contents = newFeed.getContents();
        responseDto.likeCount = 0;
        responseDto.imageUrls = newFeed.getImageUrls();
        return responseDto;
    }
}
