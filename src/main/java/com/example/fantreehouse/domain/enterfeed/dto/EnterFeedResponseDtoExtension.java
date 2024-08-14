package com.example.fantreehouse.domain.enterfeed.dto;

import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EnterFeedResponseDtoExtension {
    private Long id;
    private String title;
    private String contents;
    private String enterName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String createdAt;
    private String groupName;


    @Builder
    public EnterFeedResponseDtoExtension(Long id, String title, String contents, String enterName, LocalDate date, String createdAt, String groupName) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.enterName = enterName;
        this.date = date;
        this.createdAt = createdAt;
        this.groupName = groupName;
    }

    public static EnterFeedResponseDtoExtension ofNotice(EnterFeed feed, String groupName) {
        return EnterFeedResponseDtoExtension.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .contents(feed.getContents())
                .enterName(feed.getEnterName())
                .createdAt(feed.getCreatedAt())
                .groupName(groupName)
                .build();
    }

    public static EnterFeedResponseDtoExtension ofSchedule(EnterFeed feed, String groupName) {
        return EnterFeedResponseDtoExtension.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .contents(feed.getContents())
                .enterName(feed.getEnterName())
                .date(feed.getScheduleDate())
                .createdAt(feed.getCreatedAt())
                .groupName(groupName)
                .build();
    }
}
