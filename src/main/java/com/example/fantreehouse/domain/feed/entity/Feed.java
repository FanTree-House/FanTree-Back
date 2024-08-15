package com.example.fantreehouse.domain.feed.entity;

import static com.example.fantreehouse.common.enums.ErrorType.UNAUTHORIZED;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.comment.entity.Comment;
import com.example.fantreehouse.domain.feed.dto.request.CreateFeedRequestDto;
import com.example.fantreehouse.domain.feed.dto.request.UpdateFeedRequestDto;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
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

    @Column(nullable = false)
    private String artistName; //작성한 artist 활동명

    @Column(nullable = false)
    private String contents;

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    private int feedLikeCount;



    // 댓글이랑 일대다
    @OneToMany(mappedBy = "feed")
    private List<Comment> comments = new ArrayList<>();

    // 사용자랑 다대일
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 아티스트 그룹이랑 다대일
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_group_id")
    private ArtistGroup artistGroup;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedLike> feedLikeList = new ArrayList<>();

    @Builder
    public Feed(String artistName, String contents, List<String> imageUrls, User user, ArtistGroup artistGroup) {
        this.artistName = artistName;
        this.contents = contents;
        this.imageUrls = imageUrls;
        this.feedLikeCount = 0;
        this.user = user;
        this.artistGroup = artistGroup;
    }

    public void updateImageUrls(List<String> imageUrls) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);

    }

    public static Feed of(CreateFeedRequestDto requestDto, User user, ArtistGroup artistGroup ) {
        return Feed.builder()
                .artistName(user.getArtist().getArtistName())
                .contents(requestDto.getContents())
                .user(user)
                .artistGroup(artistGroup)
                .imageUrls(new ArrayList<>())
                .build();
    }

    public void updateFeed(UpdateFeedRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void checkArtistGroup(User user, String groupName) {
        if (!user.getArtist().getArtistGroup().getGroupName().equals(groupName)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }
}
