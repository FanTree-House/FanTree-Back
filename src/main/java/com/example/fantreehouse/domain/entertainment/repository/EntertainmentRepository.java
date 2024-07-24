package com.example.fantreehouse.domain.entertainment.repository;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
}
