package com.team14.backend.repository;

import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Record,Long> {
    List<Record> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
