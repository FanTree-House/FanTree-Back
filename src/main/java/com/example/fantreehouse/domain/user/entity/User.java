package com.example.fantreehouse.domain.user.entity;


import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.commentLike.entity.CommentLike;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.product.pickup.entity.PickUp;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import com.example.fantreehouse.domain.user.dto.AdminRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Getter
@Setter

@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long id;

    private String loginId; // 로그인할때 쓰는 username

    private String name; // 실명

    private String nickname; // 로그인 한 닉네임

    @Email
    private String email;

    private String password;

    private String profilePicture;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    private String statusUpdate;

    private String refreshToken;

    @Column
    private Long kakaoId;

    //아티스트 피드와 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedLike> feedLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    //픽업데이터와 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<PickUp> pickUpList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommunityLike> likeList = new ArrayList<>();

    //엔터테이너먼트와 일대일 매핑
    @OneToOne         // 주인
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    @OneToOne(mappedBy = "user")
    private Artist artist;

    //구독자와 일대다 매핑
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Subscription> subscriptions = new ArrayList<>();

    //구독자 커뮤니티랑 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<CommunityFeed> communityFeedList = new ArrayList<>();

    //엔터 피드와 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<EnterFeed> enterFeedList = new ArrayList<>();

    // 커뮤니티 댓글과 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<CommunityComment> communityCommentList = new ArrayList<>();

    @Builder
    public User(String loginId, String name, String nickname,
        String email, String password, String profilePicture, UserRoleEnum userRole) {
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.status = UserStatusEnum.ACTIVE_USER;
        this.userRole = userRole;
    }

    public void withDraw() {
        this.status = UserStatusEnum.WITHDRAW_USER;
        this.statusUpdate = this.getModifiedAt();
        this.refreshToken = null;
    }

    public boolean logout() {
        refreshToken = null;
        return refreshToken == null ? true : false;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void update(Optional<String> email, Optional<String> newEncodePw) {
        this.email = email.orElse(this.email);
        this.password = newEncodePw.orElse(this.password);
    }

    public User(String loginId, String name, String nickname, String email, String password, UserRoleEnum userRole, UserStatusEnum status, Long kakaoId) {
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
        this.kakaoId = kakaoId;
    }

    public void kakaoIdUpdate(Long kakaoId) {
        this.kakaoId=kakaoId;
    }

    public void transBlacklist() {
        this.status = UserStatusEnum.BLACK_LIST;
    }

    public void transRole(UserRoleEnum roleEnum) {
        this.userRole = roleEnum;
    }

    public void transUser() {
        this.status = UserStatusEnum.ACTIVE_USER;
    }
}