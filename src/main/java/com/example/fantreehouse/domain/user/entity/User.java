package com.example.fantreehouse.domain.user.entity;


import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.merch.pickup.entity.PickUp;
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
    @Column(nullable = false, unique = true)
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

    @OneToMany(mappedBy = "user")
    private List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PickUp> pickUpList = new ArrayList<>();


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
}
