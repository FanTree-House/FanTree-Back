package com.example.fantreehouse.domain.user.entity;


import static com.example.fantreehouse.common.enums.ErrorType.NOT_YOUR_ENTERTAINMENT;
import static com.example.fantreehouse.common.enums.ErrorType.UNAUTHORIZED;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.UnAuthorizedException;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.commentLike.entity.CommentLike;
import com.example.fantreehouse.domain.communityLike.entitiy.CommunityLike;
import com.example.fantreehouse.domain.communitycomment.entity.CommunityComment;
import com.example.fantreehouse.domain.communityfeed.entity.CommunityFeed;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.feedlike.entity.FeedLike;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


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

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    private String statusUpdate;

    private String refreshToken;

    private LocalDateTime lastLoginDate;

    @Column
    private Long kakaoId;

    //아티스트 피드와 일대다 매핑
    @OneToMany(mappedBy = "user")
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedLike> feedLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<CommentLike> commentLikeList = new ArrayList<>();

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
        String email, String password, UserRoleEnum userRole) {
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImageUrl = "default";
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
        return true;
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

    public void updateImageUrl(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void updateEntertainment(Entertainment entertainment) {
        this.entertainment = entertainment;
    }

    public void setInactive() {
        this.status = UserStatusEnum.INACTIVE_USER;
    }

    public boolean isInactive() {
        return this.status == UserStatusEnum.INACTIVE_USER;
    }

    public void setLogin(){
        this.lastLoginDate = LocalDateTime.now();
    }

    public void activateUser(){
        this.status = UserStatusEnum.ACTIVE_USER;
    }

    public boolean isAdmin(){
        return this.userRole == UserRoleEnum.ADMIN;
    }

    public boolean isArtist(){return this.userRole == UserRoleEnum.ARTIST;}

    public boolean isEntertainment(){return this.userRole == UserRoleEnum.ENTERTAINMENT;}

    /**
     * [verifyEntertainmentAuthority] 사용자의 권한과 본인인지 확인합니다.
     * @param user 사용자 객체
     */
    public void validationUser(User user){
        if (user.isAdmin()){
            return;
        }
        if (!(user.getUserRole().equals(UserRoleEnum.ARTIST) ||
            user.getUserRole().equals(UserRoleEnum.ENTERTAINMENT))) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
        if (!this.id.equals(user.getId())){
            if (!this.entertainment.getId().equals(user.getEntertainment().getId())) {
                throw new UnAuthorizedException(NOT_YOUR_ENTERTAINMENT);
            }
        }
    }

    public void validationEnterAndAdmin(User user){
        if (user.isAdmin()){
            return;
        }
        if (!user.getUserRole().equals(UserRoleEnum.ENTERTAINMENT)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
        if (!this.id.equals(user.getId())){
            if (!this.entertainment.getId().equals(user.getEntertainment().getId())) {
                throw new UnAuthorizedException(NOT_YOUR_ENTERTAINMENT);
            }
        }
    }
    /**
     * [verifyEntertainmentAuthority] 사용자가 엔터테인먼트 권한 가지고 있는지 확인합니다.
     * @param user 사용자 객체
     */
    public void verifyEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ENTERTAINMENT.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }
    }

    public void checkUserRole(UserRoleEnum userRoleEnum) {
        if (!userRoleEnum.equals(UserRoleEnum.ARTIST)) {
            throw new UnAuthorizedException(UNAUTHORIZED);
        }
    }

}