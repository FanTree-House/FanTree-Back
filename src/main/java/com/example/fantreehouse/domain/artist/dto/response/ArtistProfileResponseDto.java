package com.example.fantreehouse.domain.artist.dto.response;

import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import lombok.Getter;

@Getter
public class ArtistProfileResponseDto {

    private String artistName; //활동명
    private String artistProfilePicture;
    private Long subscriberCount;
    private ArtistGroup artistGroup;

    public ArtistProfileResponseDto(String artistName, String artistProfilePicture,
                                    Long subscriberCount, ArtistGroup artistGroup) {
        this.artistName = artistName;
        this.artistProfilePicture = artistProfilePicture;
        this.subscriberCount = subscriberCount;
        this.artistGroup = artistGroup;
    }

    public static ArtistProfileResponseDto of(Artist artist) {
        return new ArtistProfileResponseDto(
                artist.getArtistName(),
                artist.getArtistProfilePicture(),
                artist.getSubscriberCount(),
                artist.getArtistGroup()
        );
    }

}
