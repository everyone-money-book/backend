package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import com.team14.backend.repository.FollowRepository;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseDto follow(HashMap<String, String> map, UserDetailsImpl userDetails) {
        String followUsername = map.get("username");
        User followUser = userRepository.findByUsername(followUsername).orElseThrow(
                () -> new CustomErrorException("해당 유저가 없습니다")
        );

        Long id = userDetails.getUser().getId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomErrorException("로그인을 해주세요")
        );
        Follow follow = new Follow(user, followUser);
        followRepository.save(follow);

        user.getFollowings().add(follow);
        followUser.getFollowers().add(follow);

        return new ResponseDto("success", "구독하였습니다", follow);
    }

    @Transactional
    public ResponseDto unfollow(HashMap<String, String> map, UserDetailsImpl userDetails) {
        String followUsername = map.get("username");
        User followUser = userRepository.findByUsername(followUsername).orElseThrow(
                () -> new CustomErrorException("해당 유저가 없습니다")
        );

        Long id = userDetails.getUser().getId();
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomErrorException("로그인을 해주세요")
        );
        Follow follow = followRepository.findByFromUserAndToUser(user, followUser);
        Long followId = follow.getId();
        followRepository.deleteById(followId);
        user.getFollowings().remove(follow);
        followUser.getFollowers().remove(follow);

        return new ResponseDto("success", "구독취소하였습니다", follow);
    }
}
