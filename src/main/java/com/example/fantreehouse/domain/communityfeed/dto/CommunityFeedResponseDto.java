package com.example.fantreehouse.domain.communityfeed.dto;

import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommunityFeedResponseDto {

    String nickname;
    String contents;
    List<String> imageUrls = new ArrayList<>();

    public CommunityFeedResponseDto(CommunityFeed feed) {
        this.nickname = feed.getNickname();
        this.contents = feed.getContents();
        this.imageUrls = feed.getImageUrls();
    }
}
