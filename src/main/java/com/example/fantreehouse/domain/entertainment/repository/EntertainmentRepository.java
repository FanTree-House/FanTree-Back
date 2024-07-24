package com.example.fantreehouse.domain.entertainment.repository;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    Entertainment findByEnterName(String enterName);
}
