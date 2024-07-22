package com.example.fantreehouse.domain.feed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "feed")
public class Feed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contents;

    @Column
    private String post_picture;

    @Column(length = 20)
    private String category;

    @Column
    private LocalDate date;

    // 댓글이랑 일대다
    @OneToMany(mappedBy = "feed")
    private List<Comment> comments = new ArrayList<>();

    // 사용자랑 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
