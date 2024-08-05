package com.example.fantreehouse.domain.feedlike.dto.response;

public class FeedLikeResponseDto {

    private Long feedId;
    private int feedLikeCount;

    public FeedLikeResponseDto(Long feedId, int feedLikeCount) {
        this.feedId = feedId;
        this.feedLikeCount = feedLikeCount;
    }

    public static FeedLikeResponseDto of(Long feedId, int feedLikeCount) {
        return new FeedLikeResponseDto(
                feedId, feedLikeCount
        );
    }
}
