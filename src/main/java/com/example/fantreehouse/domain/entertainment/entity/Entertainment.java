package com.example.fantreehouse.domain.entertainment.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
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
@Table (name = "entertainment")
public class Entertainment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long entertainment_id;

    @Column(length = 20, nullable = false, unique = true)
    private String enterName;

    @Column(nullable = false, unique = true)
    private Long enterNumber;

    @Column(nullable = false)
    private String enterLogo;

    @OneToMany(mappedBy = "entertainment")
    private List<ArtistGroup> artistGroupsList = new ArrayList<>();

    @OneToOne(mappedBy = "entertainment")
    private User user;

    public Entertainment(EntertainmentRequestDto enterRequestDto, User user) {
        this.enterName = enterRequestDto.getEnterName();
        this.enterNumber = enterRequestDto.getEnterNumber();
        this.enterLogo = enterRequestDto.getEnterLogo();
        this.user = user;
    }
}