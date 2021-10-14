package com.team14.backend.controller;

import com.team14.backend.dto.RecordQueryDto;
import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.RecordResponseDto;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.RecordService;
import com.team14.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RecordController {

    private final RecordService recordService;
    private final UserService userService;

    @Autowired
    public RecordController(RecordService recordService, UserService userService) {
        this.recordService = recordService;
        this.userService = userService;
    }

    //가계부 Get 요청
    @GetMapping("/api/records")
    public ResponseDto getRecords(@RequestBody RecordQueryDto queryDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);                                                        //로그인 상태 확인
        User user = userService.loadLoginUser(userDetails);                             //로그인 유저 정보 확인
        RecordResponseDto responseDto = recordService.getAllRecords(queryDto, user);    //가계부 조회
        return new ResponseDto("success", "", responseDto);
    }

    //가계부 작성
    @PostMapping("/api/records")
    public ResponseDto saveRecord(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);                                //로그인 상태 확인
        User user = userService.loadLoginUser(userDetails);     //로그인 유저 정보 확인
        recordService.saveRecord(requestDto, user);             //가계부 저장
        return new ResponseDto("success", "성공적으로 저장하였습니다.", "");
    }

    //가계부 수정
    @PutMapping("/api/records")
    public ResponseDto editRecord(@RequestBody RecordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);                                    //로그인 상태 확인
        if (checkRecord(requestDto.getRecordId(), userDetails)) {   //가계부 작성자와 로그인 사용자와 동인 인물인지 확인
            recordService.editRecord(requestDto);                   //가계부 수정
            return new ResponseDto("success", "성공적으로 수정되었습니다.", "");
        }
        throw new CustomErrorException("로그인 정보와 게시글의 유저 정보와 일치하지 않습니다.");
    }

    //가계부 삭제
    @DeleteMapping("/api/records")
    public ResponseDto deleteRecord(@RequestBody Map<String, Long> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);                     //로그인 상태 확인
        Long recordId = map.get("recordId");
        if (checkRecord(recordId, userDetails)) {    //가계부 작성자와 로그인 사용자와 동인 인물인지 확인
            recordService.deleteRecord(recordId);    //가계부 삭제
            return new ResponseDto("success", "성공적으로 삭제되었습니다.", "");
        }
        throw new CustomErrorException("로그인 정보와 게시글의 유저 정보와 일치하지 않습니다.");
    }

    //로그인 상태 확인
    private void checkLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 사용자만 사용가능한 기능입니다.");
        }
    }

    //가계부 작성자와 로그인 사용자와 동인 인물인지 확인
    private boolean checkRecord(Long recordId, UserDetailsImpl userDetails) {
        User user = userService.loadLoginUser(userDetails);
        Record record = recordService.loadRecord(recordId);
        return user.getId().equals(record.getUser().getId());
    }
}
