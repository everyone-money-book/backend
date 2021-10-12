package com.team14.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.UserRequestDto;
import com.team14.backend.model.Follow;
import com.team14.backend.model.User;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.KakaoUserService;
import com.team14.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Autowired
    public UserController(UserService userService, KakaoUserService kakaoUserService) {
        this.userService = userService;
        this.kakaoUserService = kakaoUserService;
    }

    //로그인정보 확인용 테스트
    @GetMapping("/")
    @ResponseBody
    public User home(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        return userDetails.getUser();
    }

    //회원가입
    @PostMapping("/api/users")
    @ResponseBody
    public ResponseDto signUp(@RequestBody @Valid UserRequestDto userRequestDto) {
        ResponseDto responseDto = userService.signup(userRequestDto);
        System.out.println(responseDto);
        return responseDto;
    }
    
    //유저네임 중복 검사
    @PostMapping("/api/users/username")
    @ResponseBody
    public ResponseDto nicknameDuplicate(@RequestBody HashMap<String, String> map) {
        String username = map.get("username");
        System.out.println(username);
        return userService.checkUsername(username);
    }

    //로그인페이지 이동
    @GetMapping("user/login")
    public String login() {
        return "login";
    }

    //카카오 로그인 인가 처리 URI
    @GetMapping("/api/users/kakao/callback")
    public String kakaologin(@RequestParam String code) throws JsonProcessingException {
        //authorizeCode : 카카오 서버로 부터 받은 인가 코드
        kakaoUserService.kakaologin(code);
        //서비스에서 다 처리 끝나면 홈으로 리다이렉트 그전에 값을 가져온 거를 로그인 처리 해줘야함
        return "redirect:/";
    }

    //회원정보가져오기
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user;
        try {
            user = userDetails.getUser();
        } catch (Exception e) {
            String result = "failed";
            String msg = "회원정보를 가져오지 못했습니다.";
            return new ResponseDto(result, msg, null);
        }
        String result = "success";
        String msg = "회원정보를 가져왔습니다";
        return new ResponseDto(result, msg, user);
    }

    //회원정보 수정하기
    @PutMapping("/api/users")
    @ResponseBody
    public ResponseDto updateUserInfo(@RequestBody HashMap<String, Object> map, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(map);
        System.out.println(userDetails);
        ResponseDto responseDto = userService.updateUserInfo(map, userDetails);
        System.out.println(responseDto);
        return responseDto;
    }

    //팔로우 테스트
    @GetMapping("/api/users/followcheck")
    @ResponseBody
    public List<User> checkFollow(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long id = userDetails.getUser().getId();

        User user = userService.findUser(id);
        List<Follow> followingList = user.getFollowings();
        List<User> userList = new ArrayList<>();
        for(Follow following : followingList){
            userList.add(following.getToUser());
        }
        return userList;
    }
}
