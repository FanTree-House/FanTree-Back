package com.example.fantreehouse.domain.artist.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.dto.request.ArtistRequestDto;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.fantreehouse.domain.s3.service.S3FileUploader.DEFAULT_URL;

@Entity
@Getter
@Table(name = "artist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String artistName; //활동명

    @Column(nullable = false)
    private String artistProfileImageUrl;

    private String introduction;

    //아티스트 그룹과 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public Artist(String artistName, String artistProfileImageUrl, String introduction,
                  ArtistGroup artistGroup, User user) {
        this.artistName = artistName;
        this.artistProfileImageUrl = artistProfileImageUrl;
        this.introduction = introduction;
        this.artistGroup = artistGroup;
        this.user = user;
    }

    public static Artist of(ArtistRequestDto requestDto, User loginUser) {
        return Artist.builder()
                .artistName(requestDto.getArtistName())
                .artistProfileImageUrl(DEFAULT_URL)
                .introduction(requestDto.getIntroduction())
                .user(loginUser)
                .build();
    }

    public void setArtistGroup(ArtistGroup artistGroup) {
        if (this.artistGroup != null) {
            this.artistGroup.getArtists().remove(this);
        }
        this.artistGroup = artistGroup;
        if (artistGroup != null) {
            artistGroup.getArtists().add(this);
        }
    }

    public void updateArtist(ArtistRequestDto requestDto) {
        this.artistName = requestDto.getArtistName();
        this.introduction = requestDto.getIntroduction();
    }

    public void updateImageUrl(String imageUrl) {
        this.artistProfileImageUrl = imageUrl;
    }
}