package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
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

@Controller
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    //팔로우하기
    @PostMapping("/api/follow")
    @ResponseBody
    public ResponseDto follow(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.follow(map, userDetails);
    }

    //팔로우 취소
    @DeleteMapping("/api/follow")
    @ResponseBody
    public ResponseDto unfollow(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.unfollow(map, userDetails);
    }
}
