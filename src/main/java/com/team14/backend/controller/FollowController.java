package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    //팔로우하기
    @PostMapping("/api/follow")
    public ResponseDto follow(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);
        return followService.follow(map, userDetails);
    }

    //팔로우 취소하기
    @DeleteMapping("/api/follow")
    public ResponseDto unfollow(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);
        return followService.unfollow(map, userDetails);
    }

    //로그인 여부 확인 공통메서드
    private void checkLogin(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 사용자만 사용가능한 기능입니다.");
        }
    }
}
