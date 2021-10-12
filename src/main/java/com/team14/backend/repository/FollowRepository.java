package com.team14.backend.repository;

import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFromUserAndToUser(User user, User followUser);

}
