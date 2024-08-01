package com.example.fantreehouse.domain.artistgroup.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "artist_group")
public class ArtistGroup extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String groupName;

    private String artistGroupProfileImageUrl;

    private String groupInfo;

    private String enterName;

    //엔터테이너먼트와 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    // 아티스트그룹과 아티스트와의 일대다 매핑
    @OneToMany(mappedBy = "artistGroup")
    private List<Artist> artists = new ArrayList<>();

    //엔터피드와 다대일 매핑
    @OneToMany(mappedBy = "artistGroup")
    private List<EnterFeed> enterFeedList = new ArrayList<>();

    //구독자와 일대다 관계
    @OneToMany(mappedBy = "artistGroup")
    private List<Subscription> subscriptionList = new ArrayList<>();

    //구독자 커뮤니티 피드와 일대다관계
    @OneToMany(mappedBy = "artistGroup")
    private List<CommunityFeed> communityFeedList = new ArrayList<>();

    //아티스트피드와 일대다 매핑하기
    @OneToMany(mappedBy = "artistGroup")
    private List<Feed> feedList = new ArrayList<>();


    public ArtistGroup(String groupName, String artistGroupProfileImageUrl, String groupInfo, Entertainment entertainment, String enterName) {
        this.groupName = groupName;
        this.artistGroupProfileImageUrl = artistGroupProfileImageUrl;
        this.groupInfo = groupInfo;
        this.entertainment = entertainment;
        this.enterName = enterName;
    }

    public ArtistGroup(String groupName, String groupInfo, String enterName, Entertainment entertainment) {
        this.groupName = groupName;
        this.groupInfo = groupInfo;
        this.enterName = enterName;
        this.entertainment = entertainment;
    }

    public ArtistGroup(ArtistGroup artistGroup) {
        this.groupName = artistGroup.getGroupName();
        this.artistGroupProfileImageUrl = artistGroup.getArtistGroupProfileImageUrl();
    }

    // 아티스트 추가 메서드
    public void addArtist(Artist artist) {
        // 기존 그룹에서 제거
        if (artist.getArtistGroup() != null) {
            artist.getArtistGroup().getArtists().remove(artist);
        }
        // 새 그룹에 추가
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
        this.artistGroupProfileImageUrl = artistProfilePicture;
    }

    // 아티스트 제거 메서드
    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.setArtistGroup(null);
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }


    public void updateImageUrl(String imageUrl) {
        this.artistGroupProfileImageUrl = imageUrl;
    }
}