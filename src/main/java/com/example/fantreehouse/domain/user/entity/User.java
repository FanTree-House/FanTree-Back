package com.example.fantreehouse.domain.user.entity;


import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.product.pickup.entity.PickUp;
import com.example.fantreehouse.domain.subscription.entity.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
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

    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRole;

    private String statusUpdate;

    private String refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PickUp> pickUpList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;

    @Builder
    public User(String loginId, String name, String nickname, String email, String password, UserRoleEnum userRole) {
        this.loginId = loginId;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
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
}
