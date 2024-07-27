package com.example.fantreehouse.domain.artist.repository;

import com.example.fantreehouse.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByUserId(Long id);

    boolean existsByArtistName(String artistName);

    Optional<Artist> findByUserId(Long id);
}