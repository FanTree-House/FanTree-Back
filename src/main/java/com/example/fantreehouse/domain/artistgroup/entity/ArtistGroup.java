package com.example.fantreehouse.domain.artistgroup.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "artist_group")
public class ArtistGroup extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String group_name;

    private String artistProfilePicture;

    //group과 enter의 다대일관계
    @ManyToOne
    @JoinColumn(name = "entertainment_id")
    private Entertainment entertainment;

    //group과 Artist와의 일대다관계
    @OneToMany(mappedBy = "artist_group", orphanRemoval = true)
    private List<Artist> artistAccountList;
}
