package com.example.fantreehouse.domain.artistgroup.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "artist_group")
public class ArtistGroup extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    private String artistProfilePicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    @OneToMany(mappedBy = "artistGroup", cascade = CascadeType.ALL)
    private List<Artist> artists = new ArrayList<>();

    public ArtistGroup(String groupName, String artistProfilePicture, Entertainment entertainment) {
        this.groupName = groupName;
        this.artistProfilePicture = artistProfilePicture;
        this.entertainment = entertainment;
    }

    // 아티스트 추가 메서드
    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.setArtistGroup(this);
    }

    // 기존 아티스트 목록 초기화 메서드
    public void clearArtists() {
        for (Artist artist : new ArrayList<>(artists)) {
            artist.setArtistGroup(null);
        }
        artists.clear();
    }

    // 그룹 이름 설정 메서드
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // 아티스트 프로필 사진 설정 메서드
    public void setArtistProfilePicture(String artistProfilePicture) {
        this.artistProfilePicture = artistProfilePicture;
    }
}