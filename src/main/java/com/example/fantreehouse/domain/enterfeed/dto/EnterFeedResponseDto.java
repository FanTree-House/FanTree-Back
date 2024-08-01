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
    private FeedCategory category;
    private LocalDate date;

    public EnterFeedResponseDto(Long id, String title, String contents,  FeedCategory category, LocalDate   date) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.date = date;
    }
}