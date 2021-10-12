package com.team14.backend.repository;

import com.team14.backend.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findAllByUserIdAndCategory(Long userId, String category, Pageable pageable);
    List<Record> findAllByUserIdAndDateBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Record> findAllByUserIdAndCategoryAndDateBetween(Long userId, String category, LocalDateTime start, LocalDateTime end);

//    Page<Record> findAllByUserId(Long userId, Pageable pageable);
}
