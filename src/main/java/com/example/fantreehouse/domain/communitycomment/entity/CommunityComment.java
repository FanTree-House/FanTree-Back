package com.example.fantreehouse.domain.communitycomment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.communitycomment.dto.CommunityCommentRequestDto;
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

    @ManyToOne
    @JoinColumn(name = "communityFeed_id")
    private CommunityFeed communityFeed;

    public CommunityComment(CommunityCommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }


    public void updateComment(CommunityCommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
