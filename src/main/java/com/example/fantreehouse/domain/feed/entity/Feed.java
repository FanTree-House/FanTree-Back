package com.example.fantreehouse.domain.feed.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //유니크 설정할지 고려중
    private String artistName; //작성한 artist 활동명

    @Column(nullable = false)
    private String contents;

    private String postPicture;

    private int likesCount;

    // 댓글이랑 일대다
    @OneToMany(mappedBy = "feed")
    private List<Comment> comments = new ArrayList<>();

    // 사용자랑 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="artist_group")
    private ArtistGroup artistGroup;

    @Builder
    public Feed(String artistName, String contents, String postPicture, User user, ArtistGroup artistGroup) {
        this.artistName = artistName;
        this.contents = contents;
        this.postPicture = postPicture;
        this.likesCount = 0;
        this.user = user;
        this.artistGroup = artistGroup;
    }

    //filePath 설정 후 사용할 것
//    public static Feed of(CreateFeedRequestDto requestDto, User user, ArtistGroup artistGroup, String filePath) {
//        return Feed.builder()
//                .artistName(requestDto.getArtistName())
//                .contents(requestDto.getContents())
//                .post_picture(filePath)
//                .user(user)
//                .artistGroup(artistGroup)
//                .build();
//    }

    //file 업로드 기능 전까지 임시 사용
    public static Feed of(CreateFeedRequestDto requestDto, User user, ArtistGroup artistGroup) {
        return com.example.fantreehouse.domain.feed.entity.Feed.builder()
                .artistName(requestDto.getArtistName())
                .contents(requestDto.getContents())
                .user(user)
                .artistGroup(artistGroup)
                .build();
    }
    //filePath 설정 후 사용할 것
//    public Feed updateFeed(UpdateFeedRequestDto requestDto, String filePath) {
//        return Feed.builder()
//                .contents(requestDto.getContents())
//                .postPicture(filePath)
//                .build();
//
//    }

    //file 업로드 기능 전까지 임시 사용
    public void updateFeed(UpdateFeedRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.postPicture = requestDto.getPostPicture();
    }
}
