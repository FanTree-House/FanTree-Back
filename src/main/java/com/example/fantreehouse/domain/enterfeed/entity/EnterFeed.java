package com.example.fantreehouse.domain.enterfeed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "enter_feed")
public class EnterFeed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String feedId;

    @ManyToOne
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    @ManyToOne
    @JoinColumn(name = "artistGroup_id")
    private ArtistGroup artistGroup;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false)
    private String contents;

    @Setter
    private String postPicture;

    @Setter
    @Column(nullable = false)
    private String category;

    @Setter
    private String date;

    public EnterFeed(String feedId, Entertainment entertainment, ArtistGroup artistGroup, User user, String contents, String postPicture, String category, String date) {
        this.feedId = feedId;
        this.entertainment = entertainment;
        this.artistGroup = artistGroup;
        this.user = user;
        this.contents = contents;
        this.postPicture = postPicture;
        this.category = category;
        this.date = date;
    }
}