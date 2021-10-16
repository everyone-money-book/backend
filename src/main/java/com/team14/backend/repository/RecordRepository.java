package com.team14.backend.repository;

import com.team14.backend.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
    List<Record> findAllByUserIdAndCategoryAndDateBetween(Long userId, String category, LocalDate start, LocalDate end);
    List<Record> findAllByUserIdOrderByDateDesc(Long userId);
    List<Record> findAllByUserIdAndCategoryOrderByDateDesc(Long userId, String category);
}
