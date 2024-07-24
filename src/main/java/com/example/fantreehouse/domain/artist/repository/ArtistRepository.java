package com.example.fantreehouse.domain.artist.repository;

import com.example.fantreehouse.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

}