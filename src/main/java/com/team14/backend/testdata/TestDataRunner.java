//package com.team14.backend.testdata;
//
//import com.team14.backend.dto.RecordRequestDto;
//import com.team14.backend.model.User;
//import com.team14.backend.model.UserRoleEnum;
//import com.team14.backend.repository.UserRepository;
//import com.team14.backend.security.UserDetailsImpl;
//import com.team14.backend.service.FollowService;
//import com.team14.backend.service.RecordService;
//import com.team14.backend.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//
//@Component
//public class TestDataRunner implements ApplicationRunner {
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    RecordService recordService;
//
//    @Autowired
//    FollowService followService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        User test1 = new User("member1",passwordEncoder.encode("1234asdf"),"male",30L,"developer",1000000L, UserRoleEnum.USER);
//        User test2 = new User("member2",passwordEncoder.encode("1234asdf"),"female",30L,"선생님",1000000L, UserRoleEnum.USER);
//        User test3 = new User("member3",passwordEncoder.encode("1234asdf"),"male",30L,"developer",1000000L, UserRoleEnum.USER);
//
//        User user1 = userRepository.save(test1);
//        User user2 = userRepository.save(test2);
//        User user3 = userRepository.save(test3);
//
//        테스트게시물올리기(user1, "2021-10-10", "식비","돈까스 먹음", 5000L );
//        테스트게시물올리기(user2, "2021-10-10", "옷","옷 새로 샀다", 10000L );
//        테스트게시물올리기(user2, "2021-10-10", "옷","옷 새로 샀다", 10000L );
//        테스트게시물올리기(user2, "2021-10-10", "옷","옷 새로 샀다", 10000L );
//        테스트게시물올리기(user2, "2021-10-10", "식비","스파게티", 5000L );
//        테스트게시물올리기(user3, "2021-10-10", "교통비","기름값", 30000L );
//
//        //user1이 user2만 구독
//        구독하기(user1, user2);
//
//    }
//
//    private void 구독하기(User user1, User user2) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("username",user2.getUsername());
//        followService.follow(map,new UserDetailsImpl(user1));
//    }
//
//    private void 테스트게시물올리기(User user1, String date, String category, String contents, Long price) {
//        Long user1Id = user1.getId();
//        RecordRequestDto requestDto1 = new RecordRequestDto(
//                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                contents,
//                category,
//                price,
//                user1Id
//        );
//        recordService.saveRecord(requestDto1, user1);
//    }
//
//
//}