package com.example.fantreehouse.domain.entertainment.dto;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import lombok.Getter;

@Getter
public class EntertainmentResponseDto {
    private Long id;
    private String enterName;
    private Long enterNumber;
    private String file;

    public EntertainmentResponseDto(Entertainment entertainment) {
        this.id = entertainment.getId();
        this.enterName = entertainment.getEnterName();
        this.enterNumber = entertainment.getEnterNumber();
        this.file = entertainment.getEnterLogo();
    }
}