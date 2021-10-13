package com.team14.backend.testdata;

import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.RecordResponseDto;
import com.team14.backend.dto.UserRequestDto;
import com.team14.backend.model.User;
import com.team14.backend.model.UserRoleEnum;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
import com.team14.backend.service.RecordService;
import com.team14.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TestDataRunner implements ApplicationRunner {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RecordService recordService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User test1 = new User("member1","1234asdf","male",30L,"developer",1000000L, UserRoleEnum.USER)
        User test2 = new User("member1","1234asdf","male",30L,"developer",1000000L, UserRoleEnum.USER)
        User test3 = new User("member1","1234asdf","male",30L,"developer",1000000L, UserRoleEnum.USER)

        User user1 = userRepository.save(test1);
        User user2 = userRepository.save(test1);
        User user3 = userRepository.save(test1);

        //user2와 user3가 피드 올렸고, user1은 user2만 구독한 상황
        String user1Id = user1.getId()+"";
        UserDetailsImpl userDetails = new UserDetailsImpl(user1);
        RecordRequestDto requestDto1 = new RecordRequestDto(
                LocalDate.parse("2021-10-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")),               "돈까스 먹음",
                "식비",
                5000L,
                user1Id,
                null,
                0,
                0
        );
        recordService.saveRecord(requestDto1,userDetails);

    }

}
