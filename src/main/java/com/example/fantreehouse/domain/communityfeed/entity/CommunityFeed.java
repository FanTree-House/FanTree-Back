package com.example.fantreehouse.domain.communityfeed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedRequestDto;
import com.example.fantreehouse.domain.communityfeed.dto.CommunityFeedUpdateRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    //유저와 다대일 매핑
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //커뮤니티댓글과 일대다 매핑
    @OneToMany(mappedBy = "communityFeed")
    private List<CommunityComment> communityCommentList = new ArrayList<>();

    //아티스트 그룹과 다대일 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

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
