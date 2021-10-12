package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.StatisticsRequestDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/api/mypage")
    public ResponseDto getMyPageInfo(
            @RequestBody StatisticsRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new CustomErrorException("로그인 정보를 찾을 수 없습니다");
        }
        return statisticsService.getMyPageInfo(requestDto,userDetails);
    }

}
