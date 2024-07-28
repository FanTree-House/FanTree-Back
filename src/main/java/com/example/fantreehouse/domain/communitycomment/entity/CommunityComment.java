package com.example.fantreehouse.domain.communitycomment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentUpdateRequestDto;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "community_comment")
public class CommunityComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private String nickname;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "communityFeed_id")
    private CommunityFeed communityFeed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CommunityComment(CommunityCommentRequestDto requestDto, User user, CommunityFeed feed) {
        this.user = user;
        this.communityFeed = feed;
        this.contents = requestDto.getContents();
        this.nickname = user.getNickname();
    }

    public void updateComment(CommunityCommentUpdateRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.nickname = user.getNickname();
    }
}
