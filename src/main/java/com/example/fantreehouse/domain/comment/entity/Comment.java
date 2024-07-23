package com.example.fantreehouse.domain.comment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")

public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    // Feed랑 다대일 연관관계
    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

}
