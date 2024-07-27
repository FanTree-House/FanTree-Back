package com.example.fantreehouse.domain.communityfeed.dto;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityFeedResponseDto {

    String nickname;
    String contents;
    String post_picture;

    public CommunityFeedResponseDto(CommunityFeed feed) {
        this.contents = feed.getContents();
        this.post_picture = feed.getPost_picture();
        this.nickname = feed.getNickname();
    }
}
