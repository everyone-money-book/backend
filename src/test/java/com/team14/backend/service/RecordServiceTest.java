package com.team14.backend.service;

import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.model.UserRoleEnum;
import com.team14.backend.repository.RecordRepository;
import com.team14.backend.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecordServiceTest {

    @InjectMocks
    RecordService recordService;

    @Mock
    RecordRepository recordRepository;
    @Mock
    UserRepository userRepository;

    private LocalDate date = LocalDate.of(2021,10,13);
    private String contents = "돈가스 김밥";
    private String category = "식비";
    private Long cost = 3900L;
    private String userId = "testMember";
    private Long recordId = 1L;
    private int page = 1;
    private int display = 10;
    private String username = "testMember";
    private String password = "asdf1234";
    private String sex = "남";
    private Long age = 20L;
    private String job = "선생님";
    private Long salary = 30000000L;
    private UserRoleEnum role = UserRoleEnum.USER;

    @BeforeEach
    public void setup() {
        recordService = new RecordService(recordRepository, userRepository);
    }

    @Test
    @Order(1)
    @DisplayName("가계부 저장 - 성공케이스")
    void 가계부저장(){
        //given
        RecordRequestDto requestDto = RecordRequestDto.builder()
                .date(date)
                .contents(contents)
                .category(category)
                .cost(cost).build();
        User user = new User(1L, username, password, sex, age, job, salary, role, null, null, null, null);
        Record record = new Record(2L, user, cost, contents, category, date);
//        when(recordRepository.save(record)).thenReturn(record);
        //when
        recordService.saveRecord(requestDto, user);
        //then
        Assertions.assertThat(record.getCost()).isEqualTo(cost);
    }
    
    @Test
    @Order(2)
    @DisplayName("가계부조회")
    void 가계부조회(){
        RecordRequestDto requestDto = RecordRequestDto.builder()
                .date(date)
                .contents(contents)
                .category(category)
                .cost(cost).build();
        User user = new User(username, password, sex, age, job, salary, role);
        //when
        recordService.saveRecord(requestDto, user);

        //then
    }
}
