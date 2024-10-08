package com.example.fantreehouse.domain.artistgroup.dto;

import com.example.fantreehouse.domain.artist.dto.ArtistResponseDto;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ArtistGroupResponseDto {
    private Long id;
    private String groupName;
    private String artistGroupProfileImageUrl;
    private EntertainmentResponseDto entertainmentDto;
    private List<ArtistResponseDto> artistDtos;
    private String groupInfo;
    private Long subscribeCount;

    public ArtistGroupResponseDto(Long id, String groupName, String artistGroupProfileImageUrl, EntertainmentResponseDto entertainmentDto,
                                  List<ArtistResponseDto> artistDtos, String groupInfo) {
        this.id = id;
        this.groupName = groupName;
        this.artistGroupProfileImageUrl = artistGroupProfileImageUrl;
        this.entertainmentDto = entertainmentDto;
        this.artistDtos = artistDtos;
        this.groupInfo = groupInfo;
    }

    public ArtistGroupResponseDto(ArtistGroupResponseDto ag, Long subscribeCount) {
        this.id = ag.id;
        this.groupName = ag.groupName;
        this.artistGroupProfileImageUrl = ag.artistGroupProfileImageUrl;
        this.entertainmentDto = ag.entertainmentDto;
        this.artistDtos = ag.artistDtos;
        this.subscribeCount = subscribeCount;
    }
}