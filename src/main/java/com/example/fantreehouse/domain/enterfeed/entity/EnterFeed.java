package com.example.fantreehouse.domain.enterfeed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enter_feed")

public class EnterFeed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //엔터테이너먼트와 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    //아티스트그룹과 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

    //유저와 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    private String enterName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedCategory category;

    private LocalDate scheduleDate;

    public EnterFeed(Entertainment entertainment, User user, String title, String contents, FeedCategory category,
                     LocalDate scheduleDate) {
        this.entertainment = entertainment;
        this.enterName = entertainment.getEnterName();
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.scheduleDate = scheduleDate;
    }

    public void updateContents(String title, String contents,  FeedCategory category, LocalDate date) {
        this.title = title;
        this.contents = contents;
        this.category = category;
        this.scheduleDate = date;
    }
}