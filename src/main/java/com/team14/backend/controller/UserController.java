package com.team14.backend.controller;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.UserRequestDto;
import com.team14.backend.dto.UserUpdateReqeustDto;
import com.team14.backend.dto.UsernameCheckRequestDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.KakaoUserService;
import com.team14.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Api(value = "UserController", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    //회원가입
    @PostMapping("/api/users")
    @ResponseBody
    @ApiOperation(value = "회원가입 API", notes = "회원가입form에서 정보를 받아 DB에 저장합니다")
    public ResponseDto signUp(@RequestBody @Valid UserRequestDto userRequestDto) { //유효성검사 실패시 예외처리
        return userService.signup(userRequestDto);
    }
    
    //유저네임 중복 검사
    @PostMapping("/api/users/username")
    @ResponseBody
    public ResponseDto nicknameDuplicate(@RequestBody @Valid UsernameCheckRequestDto usernameCheckRequestDto) {
        String username = usernameCheckRequestDto.getUsername();
        System.out.println(username);
        return userService.checkUsername(username);
    }
    
    //로그인 성공시 responseDto전달
    @GetMapping("/user/login/success")
    @ResponseBody
    public ResponseDto loginSuccess(){
        return new ResponseDto("success", "성공적으로 로그인이 되었습니다.", "");
    }
    
    //로그인 실패시 responseDto전달
    @GetMapping("/user/login/fail")
    @ResponseBody
    public ResponseDto loginFail(){
        return new ResponseDto("failed", "로그인에 실패하였습니다.", "");
    }
    
    //로그아웃 성공시 responseDto전달
    @GetMapping("/user/logout/success")
    @ResponseBody
    public ResponseDto logoutSuccess(){
        return new ResponseDto("success", "로그아웃 하였습니다.", "");
    }
//    //로그인페이지 이동
//    @GetMapping("user/login")
//    public String login() {
//        return "login";
//    }

    //카카오 로그인 인가 처리 URI
    @GetMapping("/api/users/kakao/callback")
    @ResponseBody
    public ResponseDto kakaologin(@RequestParam String code) {
        //authorizeCode : 카카오 서버로 부터 받은 인가 코드
        try {
            kakaoUserService.kakaologin(code);
        } catch (Exception ex) {
            throw new CustomErrorException("카카오 로그인 실패하였습니다");
        }
        return new ResponseDto("success", "카카오로그인 성공하였습니다.", "");
    }

    //회원정보가져오기
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);
        Long userId = userDetails.getUser().getId();
        User user = userService.findUser(userId);
        return new ResponseDto("success", "회원정보를 가져왔습니다", user);
    }

    //회원정보 수정하기
    @PutMapping("/api/users")
    @ResponseBody
    public ResponseDto updateUserInfo(@RequestBody UserUpdateReqeustDto userUpdateReqeustDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);
        return userService.updateUserInfo(userUpdateReqeustDto, userDetails);
    }

    //나의 팔로잉출력 테스트
    @GetMapping("/api/users/followcheck")
    @ResponseBody
    public List<User> checkFollow(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkLogin(userDetails);
        Long id = userDetails.getUser().getId();
        User user = userService.findUser(id);
        List<Follow> followingList = user.getFollowings();
        List<User> userList = new ArrayList<>();
        for(Follow following : followingList){
            userList.add(following.getToUser());
        }
        return userList;
    }
    
    //로그인 여부 확인 공통메서드
    private void checkLogin(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomErrorException("로그인 사용자만 사용가능한 기능입니다.");
        }
    }
}
