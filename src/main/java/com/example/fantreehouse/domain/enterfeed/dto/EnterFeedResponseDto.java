package com.example.fantreehouse.domain.enterfeed.dto;

import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EnterFeedResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String postPicture;
    private FeedCategory category;
    private LocalDate date;

    public EnterFeedResponseDto(Long id, String title, String contents, String postPicture, FeedCategory category, LocalDate   date) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.postPicture = postPicture;
        this.category = category;
        this.date = date;
    }
}