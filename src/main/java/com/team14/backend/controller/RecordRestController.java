package com.team14.backend.controller;

import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordRestController {

    private final RecordService recordService;

    @Autowired
    public RecordRestController(RecordService recordService) {
        this.recordService = recordService;
    }

    //가계부 Get 요청
    @GetMapping("/api/records")
    public ResponseDto getRecords(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 정보를 찾을 수 없습니다.");
        }
        return recordService.getAllRecords(requestDto, userDetails);
    }

    //가계부 작성
    @PostMapping("/api/records")
    public ResponseDto saveRecord(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 정보를 찾을 수 없습니다.");
        }
        return recordService.saveRecord(requestDto, userDetails);
    }

    //가계부 수정
    @PutMapping("/api/records")
    public ResponseDto editRecord(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 정보를 찾을 수 없습니다.");
        }
        return recordService.editRecord(requestDto, userDetails);
    }

    //가계부 삭제
    @DeleteMapping("/api/records")
    public ResponseDto deleteRecord(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 정보를 찾을 수 없습니다.");
        }
        return recordService.deleteRecord(requestDto, userDetails);
    }
}
