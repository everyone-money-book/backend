package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.UserRequestDto;
import com.team14.backend.dto.UserUpdateReqeustDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.User;
import com.team14.backend.model.UserRoleEnum;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            throw new CustomErrorException("중복된 유저네임이 존재합니다.");
        }

        //패스워드 암호화
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;

        //true면 == 관리자이면
        if (userRequestDto.isAdmin()) {
            if (!userRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomErrorException("관리자 암호가 틀려 등록이 불가능합니다.");
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
            throw new CustomErrorException("회원가입에 실패하였습니다.");
        }
        return new ResponseDto("success", "회원가입에 성공하였습니다", "");
    }
    
    //유저네임 중복체크
    public ResponseDto checkUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) {
            return new ResponseDto("success", "사용가능한 유저네임입니다.", "");
        } else {
            throw new CustomErrorException("중복된 유저네임이 존재합니다.");
        }
    }
    
    //로그인
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomErrorException("유저네임을 찾을 수 없습니다.")
        );

        // 패스워드 암호화
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomErrorException("비밀번호가 맞지 않습니다.");
        }
        return user;
    }

    //회원정보 업데이트
    @Transactional
    public ResponseDto updateUserInfo(UserUpdateReqeustDto userUpdateReqeustDto, UserDetailsImpl userDetails) {
        String sex = userUpdateReqeustDto.getSex();
        Long age = userUpdateReqeustDto.getAge();
        String job = userUpdateReqeustDto.getJob();
        Long salary = userUpdateReqeustDto.getSalary();

        Long userId = userDetails.getUser().getId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomErrorException("존재하지 않는 회원입니다.")
        );

        try {
            user.update(sex, age, job, salary);
        } catch (Exception e) {
            throw new CustomErrorException("회원정보업데이트에 실패했습니다");
        }

        return new ResponseDto("success", "회원 정보수정에 성공하였습니다.", "");
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomErrorException("회원정보를 가져오지 못했습니다.")
        );
    }

    public User loadLoginUser(UserDetailsImpl userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new CustomErrorException("로그인된 유저의 정보를 찾을 수 없습니다.")
        );
    }
}
