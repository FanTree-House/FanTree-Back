package com.example.fantreehouse.domain.artistgroup.repository;

import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistGroupRepository extends JpaRepository<ArtistGroup, Long> {
    Optional<ArtistGroup> findByEntertainmentEnternameAndGroupName(String entername, String groupName);
    List<ArtistGroup> findAllByEntertainmentEntername(String entername);

}