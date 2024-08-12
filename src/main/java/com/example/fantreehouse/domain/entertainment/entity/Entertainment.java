package com.example.fantreehouse.domain.entertainment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.enterfeed.entity.EnterFeed;
import com.example.fantreehouse.domain.entertainment.dto.EntertainmentRequestDto;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
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
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false) // User와의 관계를 매핑합니다.
    private User user;


//    public Entertainment(String enterName, Long enterNumber ,User user) {
//        this.enterName = enterName;
//        this.enterNumber = enterNumber;
//        this.enterLogo = "default";
//        this.user = user;
//    }
//
//    public Entertainment(String enterName, Long enterNumber, String enterLogo, User user) {
//        this.enterName = enterName;
//        this.enterNumber = enterNumber;
//        this.enterLogo = enterLogo;
//        this.user = user;
//    }

    public Entertainment(String enterName, Long enterNumber, User user) {
        this.enterName = enterName;
        this.enterNumber = enterNumber;
        this.enterLogo = "default";
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