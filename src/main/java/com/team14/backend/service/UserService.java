package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.UserRequestDto;
import com.team14.backend.model.User;
import com.team14.backend.model.UserRoleEnum;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    //회원가입
    public ResponseDto signup(UserRequestDto userRequestDto) {
        String nickname = userRequestDto.getUsername();

        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(nickname);
        if (found.isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
            String result = "failed";
            String msg = "중복된 회원이 존재합니다";
            ResponseDto responseDto = new ResponseDto(result, msg, null);
            return responseDto;
        }

        //패스워드 암호화
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        //true면 == 관리자이면
        //boolean 타입의 getter는 is를 붙인다
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            //role을 admin으로 바꿔준다
            role = UserRoleEnum.ADMIN;
        }

        String sex = userRequestDto.getSex();
        Long age = userRequestDto.getAge();
        String job = userRequestDto.getJob();
        Long salary = userRequestDto.getSalary();

        User user = new User(nickname, password, sex, age, job, salary, role);

        try {
            userRepository.save(user);
        }catch (Exception e){
            String result = "failed";
            String msg = "회원가입에 실패하였습니다";
            return new ResponseDto(result, msg, null);
        }
        String result = "success";
        String msg = "회원가입에 성공하였습니다";
        return new ResponseDto(result, msg, null);
    }
    
    //아이디 중복체크
    public ResponseDto checkUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) {
            String result = "success";
            String msg = "사용가능한 아이디입니다.";
            return new ResponseDto(result, msg, null);
        } else {
            String result = "failed";
            String msg = "사용 불가능한 아이디입니다.";
            return new ResponseDto(result, msg, null);
        }
    }
    
    //회원정보 없데이트
    @Transactional
    public ResponseDto updateUserInfo(HashMap<String, Object> map, UserDetailsImpl userDetails) {
        String sex = (String) map.get("sex");
        Long age = ((Number) map.get("age")).longValue();
        String job = (String) map.get("job");
        Long salary = ((Number) map.get("salary")).longValue();
        Long userId = userDetails.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("찾을 수없는 유저")
        );
        try {
            user.update(sex, age, job, salary);
        } catch (Exception e) {
            String result = "failed";
            String msg = "회원 정보수정에 실패하였습니다.";
            return new ResponseDto(result, msg, null);
        }
        String result = "success";
        String msg = "회원 정보수정에 성공하였습니다.";
        return new ResponseDto(result, msg, null);
    }
}
