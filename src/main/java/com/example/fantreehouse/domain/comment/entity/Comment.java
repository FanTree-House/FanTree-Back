package com.example.fantreehouse.domain.comment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.comment.dto.request.CommentRequestDto;
import com.example.fantreehouse.domain.comment.dto.request.CreateCommentRequestDto;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.user.entity.User;
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

    @Column(nullable = false)
    private String contents;

    private int likesCount;

    // Feed 랑 다대일 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //작성자(artist 가 작성해도 userId 등록)


    public Comment(String contents, int likesCount, Feed feed, User user) {
        this.contents = contents;
        this.likesCount = likesCount;
        this.feed = feed;
        this.user = user;
    }

    public static Comment of(CreateCommentRequestDto requestDto, Feed feed, User user) {
        return new Comment(requestDto.getContents(), 0, feed, user);
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
