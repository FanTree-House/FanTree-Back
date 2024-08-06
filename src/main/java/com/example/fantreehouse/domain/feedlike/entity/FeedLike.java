package com.example.fantreehouse.domain.feedlike.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "feed_like")
public class FeedLike extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedlike_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public FeedLike(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }

    //연관관계 편의 메서드
    public void changeFeed(Feed feed) {
        this.feed = feed;
        feed.getFeedLikeList().add(this);
    }

}
