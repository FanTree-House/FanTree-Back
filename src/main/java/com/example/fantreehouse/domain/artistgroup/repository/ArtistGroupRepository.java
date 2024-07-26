package com.example.fantreehouse.domain.artistgroup.repository;

import com.example.fantreehouse.domain.artist.entity.Artist;
import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArtistGroupRepository extends JpaRepository<ArtistGroup, Long> {
    List<ArtistGroup> findAllByEntertainmentEnterName(String enterName);

    Optional<ArtistGroup> findByEntertainmentEnterNameAndGroupName(String enterName, String groupName);

    Optional<ArtistGroup> findByGroupName(String groupName);

//    Optional<ArtistGroup> findByUserId(Long userId);
//    Optional<ArtistGroup> findByUserId(Long userId);
}