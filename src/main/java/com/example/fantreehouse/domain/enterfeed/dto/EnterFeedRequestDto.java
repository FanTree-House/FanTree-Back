package com.example.fantreehouse.domain.enterfeed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterFeedRequestDto {
    private String feedId;
    private String contents;
    private String postPicture;
    private String category;
    private String date;
}