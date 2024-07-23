package com.example.fantreehouse.domain.subscription.entity;

import com.example.fantreehouse.common.entitiy.Timestamped;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import com.example.fantreehouse.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table (name = "subscription")
public class Subscription extends Timestamped {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "artist_group")
    private ArtistGroup artistGroup;

}
