package com.team14.backend.repository;

import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Record, Long> {
//FROM_USER_ID  //TO_USER_ID
    @Query(value = "SELECT * FROM record WHERE USER_ID IN (SELECT TO_USER_ID FROM Follow WEHRE FROM_USER_ID = :userId);", nativeQuery = true)
    List<Record> getFollowingFeeds(Long userId);

    Page<Record> findAllByUserIn(List<User> list, Pageable pageable);

}
