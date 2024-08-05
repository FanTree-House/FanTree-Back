package com.example.fantreehouse.domain.enterfeed.dto;

import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EnterFeedRequestDto {
    private String title;
    private String contents;
    private String postPicture;
    private FeedCategory category;
    private LocalDate date;
}