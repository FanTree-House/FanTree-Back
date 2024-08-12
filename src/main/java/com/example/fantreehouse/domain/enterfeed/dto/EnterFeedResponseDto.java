package com.example.fantreehouse.domain.enterfeed.dto;

import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EnterFeedResponseDto {
    private Long id;
    private String title;
    private String contents;
    private FeedCategory category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    public EnterFeedResponseDto(Long id, String title, String contents, FeedCategory category,
                                LocalDate date) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.date = date;
    }
}