package com.team14.backend.model;

import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.repository.RecordRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class RecordModelTest {

    public LocalDate date = LocalDate.now();
    public String contents = "치킨";
    public String category = "식비";
    public Long cost = 21000L;
    public String userId = "member1";
    public Long recordId = 1L;
    public int page = 0;
    public int display = 10;

    @Test
    @DisplayName("성공케이스")
    void success() {
        //given
        RecordRequestDto requestDto = RecordRequestDto.builder()
                .date(date)
                .contents(contents)
                .category(category)
                .cost(cost)
                .userId(userId)
                .recordId(recordId)
                .page(page)
                .display(display)
                .build();
        //when
        Record record = new Record();
        record.updateRecord(requestDto);
        //then
        assertThat(record.getId()).isNull();
        assertThat(record.getCategory()).isEqualTo(category);
        assertThat(record.getContents()).isEqualTo(contents);
        assertThat(record.getCost()).isEqualTo(cost);
        assertThat(record.getDate()).isEqualTo(date);
        System.out.println("date = " + date);
        System.out.println("record.getDate() = " + record.getDate());
    }

    @Nested
    @DisplayName("실패케이스")
    class fail {
        @Nested
        @DisplayName("카테고리")
        class category{
            @Test
            @DisplayName("null")
            void 반환값null(){
                category = null;
                //given
                RecordRequestDto requestDto = RecordRequestDto.builder()
                        .date(date)
                        .contents(contents)
                        .category(category)
                        .cost(cost)
                        .userId(userId)
                        .recordId(recordId)
                        .page(page)
                        .display(display)
                        .build();
                //when
                Record record = new Record();
                record.updateRecord(requestDto);
                //then
                assertThat(record.getId()).isNull();
                assertThat(record.getCategory()).isEqualTo(category);
                assertThat(record.getContents()).isEqualTo(contents);
                assertThat(record.getCost()).isEqualTo(cost);
                assertThat(record.getDate()).isEqualTo(date);
            }
        }
    }
}
