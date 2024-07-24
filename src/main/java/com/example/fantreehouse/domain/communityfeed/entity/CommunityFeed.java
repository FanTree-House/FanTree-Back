package com.example.fantreehouse.domain.communityfeed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
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
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "communityFeed")
    private List<CommunityComment> communityComments;

    public CommunityFeed(CommunityFeedRequestDto requestDto, User user) {
        this.nickname = user.getNickname();
        this.contents = requestDto.getContents();
        this.post_picture = requestDto.getPost_picture();
    }

    public void updateFeed(CommunityFeedUpdateRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.contents = requestDto.getContents();
        this.post_picture = requestDto.getPost_picture();

    }
}
