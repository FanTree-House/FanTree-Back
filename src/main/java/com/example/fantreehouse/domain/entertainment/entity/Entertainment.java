package com.example.fantreehouse.domain.entertainment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "entertainment")
public class Entertainment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String enterName;

    @Column(nullable = false, unique = true)
    private Long enterNumber;

    @Column(nullable = false)
    private String enterLogo;

    //아티스트그룹과 일대다 매핑
    @OneToMany(mappedBy = "entertainment")
    private List<ArtistGroup> artistGroupsList = new ArrayList<>();

    //엔터피드와 일대다 매핑
    @OneToMany(mappedBy = "entertainment")
    private List<EnterFeed> enterFeedList = new ArrayList<>();

    //엔터와 원투원 매핑
    @OneToOne(mappedBy = "entertainment")
    private User user;

    public Entertainment(EntertainmentRequestDto enterRequestDto, User user) {
        this.enterName = enterRequestDto.getEnterName();
        this.enterNumber = enterRequestDto.getEnterNumber();
        this.enterLogo = enterRequestDto.getEnterLogo();
        this.user = user;
    }

    public void updateEnterName(String enterName) {
        this.enterName = enterName;
    }

    public void updateEnterNumber(Long enterNumber) {
        this.enterNumber = enterNumber;
    }

    public void updateEnterLogo(String enterLogo) {
        this.enterLogo = enterLogo;
    }

}