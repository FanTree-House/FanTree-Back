package com.example.fantreehouse.domain.artistgroup.repository;

import com.example.fantreehouse.domain.artistgroup.entity.ArtistGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtistGroupRepository extends JpaRepository<ArtistGroup, Long> {

    Optional<ArtistGroup> findByGroupName(String groupName);

    List<ArtistGroup> findByGroupNameContaining (String groupName, Pageable pageable);

    Optional<ArtistGroup> findByGroupNameAndId(String groupName, Long id);

    @Query("SELECT ag FROM ArtistGroup ag " +
            "JOIN ag.subscriptionList s " +
            "GROUP BY ag.id " +
            "ORDER BY COUNT(s) DESC")
    List<ArtistGroup> findTop15ArtistGroupsBySubscriptionCount();
}