package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import com.team14.backend.repository.FollowRepository;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

//    public ResponseDto follow(HashMap<String, String> map, UserDetailsImpl userDetails) {
//        String followUsername = map.get("username");
//        User followUser = userRepository.findByUsername(followUsername).orElseThrow(
//                () -> new NullPointerException("해당 유저가 없습니다")
//        );
//
//        User user = userDetails.getUser();
//        Follow follow = new Follow(user, followUser);
//
//    }
}
