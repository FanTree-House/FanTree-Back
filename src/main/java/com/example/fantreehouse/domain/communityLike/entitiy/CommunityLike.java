package com.example.fantreehouse.domain.communityLike.entitiy;

import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
@Table(name = "communityLikes")

public class CommunityLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private Long likes;

    private boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "communityFeed_Id")
    private CommunityFeed communityFeed;

    @ManyToOne
    @JoinColumn(name = "communityComment_id")
    private CommunityComment communityComment;


    public CommunityLike(User user, CommunityFeed feed) {
        this.user = user;
        this.communityFeed = feed;
        this.status = true;
    }
}
