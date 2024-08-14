package com.example.fantreehouse.domain.communityfeed.dto;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityFeedResponseDtoExtention {

    private Long id;
    private String nickname;
    private String contents;
    private List<String> imageUrls;
    private Long likeCount;

    public CommunityFeedResponseDtoExtention(Long id, String nickname, String contents, List<String> imageUrls, Long likeCount) {
        this.id = id;
        this.nickname = nickname;
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.likeCount = likeCount;
    }

    public static CommunityFeedResponseDtoExtention of(CommunityFeed feed, Long likeCount) {
        return new CommunityFeedResponseDtoExtention(
                feed.getId(),
                feed.getNickname(),
                feed.getContents(),
                feed.getImageUrls(),
                likeCount
        );
    }
}
