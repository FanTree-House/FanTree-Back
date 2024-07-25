package com.example.fantreehouse.domain.artist.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "artist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String artistName;

    private Long artistRank;

    private Long subscriberCount;

    @Column(nullable = false)
    private String artistProfilePicture;

    //아티스트 그룹과 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

    //유저와 다 대 일 매핑
    @OneToOne(mappedBy = "artist")
    private User user;

    public void setArtistGroup(ArtistGroup artistGroup) {
        if (this.artistGroup != null) {
            this.artistGroup.getArtistList().remove(this);
        }
        this.artistGroup = artistGroup;
        if (artistGroup != null) {
            artistGroup.getArtistList().add(this);
        }
    }
}