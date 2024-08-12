package com.example.fantreehouse.domain.entertainment.repository;

import com.example.fantreehouse.domain.entertainment.entity.Entertainment;
import com.example.fantreehouse.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntertainmentRepository extends JpaRepository<Entertainment, Long> {
    Optional<Entertainment> findByEnterNumber(Long enterNumber);

    Optional<Entertainment> findByEnterName(String enterName);

    Optional<Entertainment> findByUser(User user);

}