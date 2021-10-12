package com.team14.backend.controller;

import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.service.RecordService;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordRestController {

    private final RecordService recordService;

    public RecordRestController(RecordService recordService) {
        this.recordService = recordService;
    }

    //가계부 Get 요청
    @GetMapping("/api/records")
    public ResponseDto getRecords(@RequestBody RecordRequestDto requestDto) {
        return recordService.getAllRecords(requestDto);
    }

    //가계부 작성
    @PostMapping("/api/records")
    public ResponseDto saveRecord(@RequestBody RecordRequestDto requestDto) {
        return recordService.saveRecord(requestDto);
    }

    //가계부 수정
    @PutMapping("/api/records")
    public ResponseDto editRecord(@RequestBody RecordRequestDto requestDto) {
        return recordService.editRecord(requestDto);
    }

    //가계부 삭제
    @DeleteMapping("/api/records")
    public ResponseDto deleteRecord(@RequestBody RecordRequestDto requestDto) {
        return recordService.deleteRecord(requestDto);
    }
}
