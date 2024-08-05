package com.example.fantreehouse.domain.artist.dto;

import com.example.fantreehouse.domain.user.entity.User;
import lombok.Getter;

@Getter
public class ArtistResponseDto {

    private Long id;
    private String artistName; //활동명
    private String introduction;
    private String artistProfileImageUrl;
    private User user;

    public ArtistResponseDto(Long id, String artistName) {
        this.id = id;
        this.artistName = artistName;
        this.introduction = introduction;
        this.artistProfileImageUrl = artistProfileImageUrl;
        this.user = user;
    }

}