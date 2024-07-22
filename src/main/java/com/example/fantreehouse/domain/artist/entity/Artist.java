package com.example.fantreehouse.domain.artist.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
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
    private String groupName;

    private int rank;

    private Long subscriberCount;

    @Column(nullable = false)
    private String artistProfilePicture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entertainment_id", nullable = false)
    private Entertainment entertainment;

    @ManyToOne
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "artist")
    private List<Subscription> subscriptioinList = new ArrayList<>();


    // Feed 와 일대다 연관관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feedList = new ArrayList<>();

}
