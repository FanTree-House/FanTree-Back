package com.example.fantreehouse.domain.feed.dto.response;

import com.example.fantreehouse.domain.feed.entity.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class FeedResponseDto {

    private Long id;
    private String artistName;
    private String contents;
    private List<String> imageUrls;
    private Long likesCount;
    private String profileUrl;


    public FeedResponseDto(Long id, String contents, List<String> imageUrls, Long likesCount, String artistName, String profileUrl) {
        this.id = id;
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.likesCount = likesCount;
        this.artistName = artistName;
        this.profileUrl = profileUrl;
    }

    public static FeedResponseDto of(Feed feed, Long feedLikeCount, List<String> imageUrls, Long id, String artistName, String profileUrl) {
        return new FeedResponseDto(
                id,
                feed.getContents(),
                imageUrls,
                feedLikeCount,
                artistName,
                profileUrl
        );
    }

    public static FeedResponseDto of(Feed feed, Long feedLikeCount) {
        return new FeedResponseDto(
                feed.getId(),
                feed.getContents(),
                feed.getImageUrls(),
                feedLikeCount,
                feed.getArtistName(),
                feed.getUser().getProfileImageUrl()
        );

    }
}
