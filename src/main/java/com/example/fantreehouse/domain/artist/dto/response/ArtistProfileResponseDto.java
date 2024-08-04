package com.example.fantreehouse.domain.artist.dto.response;

import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import lombok.Getter;

@Getter
public class ArtistProfileResponseDto {

    private String artistName; //활동명
    private String getArtistProfileImageUrl;
    private String artistGroupName;
    private String introduction;

    public ArtistProfileResponseDto(String artistName, String getArtistProfileImageUrl,
                                    String artistGroupName, String introduction) {
        this.artistName = artistName;
        this.getArtistProfileImageUrl = getArtistProfileImageUrl;
        this.artistGroupName = artistGroupName;
        this.introduction = introduction;
    }

    public static ArtistProfileResponseDto of(Artist artist) {
        return new ArtistProfileResponseDto(
                artist.getArtistName(),
                artist.getArtistProfileImageUrl(),
                artist.getArtistGroup().getGroupName(),
                artist.getIntroduction()
        );
    }

    public static ArtistProfileResponseDto of(Artist artist, String url) {
        return new ArtistProfileResponseDto(
                artist.getArtistName(),
                url,
                artist.getArtistGroup().getGroupName(),
                artist.getIntroduction()
        );
    }

}
