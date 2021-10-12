package com.team14.backend.repository;

import com.team14.backend.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Record, Long> {
//    Page<Record> findAllByUserId(Long userId, Pageable pageable);
//FROM_USER_ID  //TO_USER_ID
    @Query(value = "SELECT * FROM record WHERE USER_ID IN (SELECT FROM_USER_ID FROM FROM_USER_ID = :userId);", nativeQuery = true)
    Page<Record> getFollowingFeeds(Long userId);

}
