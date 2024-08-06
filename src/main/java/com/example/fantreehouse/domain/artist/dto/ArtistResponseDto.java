package com.example.fantreehouse.domain.artist.dto;

import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class ArtistResponseDto {

    private Long id;
    private String artistName; //활동명
    private String introduction;
    private String artistProfileImageUrl;

    public ArtistResponseDto(Long id, String artistName, String introduction, String artistProfileImageUrl) {
        this.id = id;
        this.artistName = artistName;
        this.introduction = introduction;
        this.artistProfileImageUrl = artistProfileImageUrl;
    }

}