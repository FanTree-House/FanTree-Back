package com.example.fantreehouse.domain.communityfeed.dto;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityFeedResponseDtoExtension {

    private Long id;
    private String nickname;
    private String contents;
    private List<String> imageUrls;
    private Long likeCount;

    public CommunityFeedResponseDtoExtension(Long id, String nickname, String contents, List<String> imageUrls, Long likeCount) {
        this.id = id;
        this.nickname = nickname;
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.likeCount = likeCount;
    }

    public static CommunityFeedResponseDtoExtension of(CommunityFeed feed, Long likeCount) {
        return new CommunityFeedResponseDtoExtension(
                feed.getId(),
                feed.getNickname(),
                feed.getContents(),
                feed.getImageUrls(),
                likeCount
        );
    }
}
