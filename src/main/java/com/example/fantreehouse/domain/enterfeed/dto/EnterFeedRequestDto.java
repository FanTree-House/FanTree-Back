package com.example.fantreehouse.domain.enterfeed.dto;

import com.example.fantreehouse.domain.enterfeed.entity.FeedCategory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter

public class EnterFeedRequestDto {
    private String title;
    private String contents;
    @Setter
    private FeedCategory category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    public EnterFeedRequestDto(String title, String contents, LocalDate scheduleDate, FeedCategory category) {
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.category = category;
    }

    public EnterFeedRequestDto(String title, String contents, FeedCategory category)
    {
        this.title = title;
        this.contents = contents;
        this.category = category;
    }
}
