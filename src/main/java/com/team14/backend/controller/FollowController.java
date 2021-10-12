package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

//    //팔로우하기
//    @PostMapping("/api/follow")
//    @ResponseBody
//    public ResponseDto follow(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return followService.follow(map, userDetails);
//    }
}
