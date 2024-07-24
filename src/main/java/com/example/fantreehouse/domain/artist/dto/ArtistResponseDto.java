package com.example.fantreehouse.domain.artist.dto;

import lombok.Getter;

@Getter
public class ArtistResponseDto {
    private Long id;
    private String name;

    public ArtistResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}