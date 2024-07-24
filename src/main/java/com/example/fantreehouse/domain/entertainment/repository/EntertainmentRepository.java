package com.example.fantreehouse.domain.entertainment.repository;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    Optional<Entertainment> findByEnterName(String enterName);
}