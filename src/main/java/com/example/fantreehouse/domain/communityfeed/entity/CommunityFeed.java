package com.example.fantreehouse.domain.communityfeed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "community_feed")
public class CommunityFeed extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;
    private String post_picture;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "communityFeed")
    private List<CommunityComment> communityComments;
}
