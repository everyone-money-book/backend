package com.team14.backend.repository;

import com.team14.backend.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findAllByCategory(String category, Pageable pageable);
    List<Record> findAllByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Record> findAllByCategoryAndDateBetween(String category, LocalDateTime start, LocalDateTime end);

//    Page<Record> findAllByUserId(Long userId, Pageable pageable);
}
