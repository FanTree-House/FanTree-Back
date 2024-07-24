package com.example.fantreehouse.domain.enterfeed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EnterFeedResponseDto {
    private String feedId;
    private String contents;
    private String postPicture;
    private String category;
    private String date;

    public EnterFeedResponseDto(String feedId, String contents, String postPicture, String category, String date) {
        this.feedId = feedId;
        this.contents = contents;
        this.postPicture = postPicture;
        this.category = category;
        this.date = date;
    }
}