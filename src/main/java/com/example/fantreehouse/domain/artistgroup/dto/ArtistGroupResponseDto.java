package com.example.fantreehouse.domain.artistgroup.dto;

import com.example.fantreehouse.domain.artist.dto.ArtistResponseDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ArtistGroupResponseDto {
    private Long id;
    private String groupName;
    private String artistProfilePicture;
    private EntertainmentResponseDto entertainmentDto;
    private List<ArtistResponseDto> artistDtos;
    private String enterName;

    public ArtistGroupResponseDto(Long id, String groupName, String artistProfilePicture, EntertainmentResponseDto entertainmentDto, List<ArtistResponseDto> artistDtos, String enterName) {
        this.id = id;
        this.groupName = groupName;
        this.artistProfilePicture = artistProfilePicture;
        this.entertainmentDto = entertainmentDto;
        this.artistDtos = artistDtos;
        this.enterName = enterName;
    }



}