package com.example.fantreehouse.domain.entertainment.dto;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.user.entity.User;
import lombok.Getter;

@Getter
public class EntertainmentResponseDto {
    private String enterName;
    private Long enterNumber;
    private String enterLogo;

    public EntertainmentResponseDto(Entertainment entertainment) {
        this.enterName = entertainment.getEnterName();
        this.enterNumber = entertainment.getEnterNumber();
        this.enterLogo = entertainment.getEnterLogo();
    }

}

    private Long id;
    private String entername;

    public EntertainmentResponseDto(Long id, String entername) {
        this.id = id;
        this.entername = entername;
    }
}