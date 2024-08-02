package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FeedResponseDto {

    private String contents;
    private List<String> imageUrls;
    private int likesCount;
    private String writeDate;

    public FeedResponseDto(String contents, List<String> imageUrls, int likesCount, String writeDate) {
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.likesCount = likesCount;
        this.writeDate = writeDate;
    }

    public static FeedResponseDto of(Feed feed, int feedLikeCount, List<String> imageUrls) {
        return new FeedResponseDto(
                feed.getContents(),
                imageUrls,
                feedLikeCount,
                feed.getCreatedAt()
        );
    }

    public static FeedResponseDto of(Feed feed, int feedLikeCount) {
        return new FeedResponseDto(
                feed.getContents(),
                feed.getImageUrls(),
                feedLikeCount,
                feed.getCreatedAt()
        );

    }
}
