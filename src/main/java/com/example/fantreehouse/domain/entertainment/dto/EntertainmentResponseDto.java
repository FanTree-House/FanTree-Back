package com.example.fantreehouse.domain.entertainment.dto;

import lombok.Getter;

@Getter
public class EntertainmentResponseDto {
    private Long id;
    private String entername;

    public EntertainmentResponseDto(Long id, String entername) {
        this.id = id;
        this.entername = entername;
    }
}