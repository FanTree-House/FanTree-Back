package com.example.fantreehouse.domain.artistgroup.dto;

import com.example.fantreehouse.domain.artist.dto.ArtistResponseDto;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class ArtistGroupResponseDto {
    private Long id;
    private String groupName;
    private String artistProfilePicture;
    private EntertainmentResponseDto entertainment;
    private List<ArtistResponseDto> artists;

    public ArtistGroupResponseDto(Long id, String groupName, String artistProfilePicture, EntertainmentResponseDto entertainment, List<ArtistResponseDto> artists) {
        this.id = id;
        this.groupName = groupName;
        this.artistProfilePicture = artistProfilePicture;
        this.entertainment = entertainment;
        this.artists = artists;
    }

    public ArtistGroupResponseDto(ArtistGroup artistGroup) {
        this.id = artistGroup.getId();
        this.groupName = artistGroup.getGroupName();
        this.artistProfilePicture = artistGroup.getArtistProfilePicture();
    }

}