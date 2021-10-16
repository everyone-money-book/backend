package com.team14.backend.service;

import com.team14.backend.dto.RecordQueryDto;
import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.RecordResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.repository.RecordRepository;
import com.team14.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    //가계부 전체 조회
    public RecordResponseDto getAllRecords(String username, LocalDate date, String category, User user) {
        Long userId;
        //쿼리문에 userId 조건이 없을 경우 자신의 가계부 전체 조회
        if (username.equals("")) {
            userId = user.getId();
        } else {    //특정 user의 가계부 전체 조회
            userId = userRepository.findByUsername(username).orElseThrow(
                    () -> new CustomErrorException("해당 유저 정보가 존재하지 않습니다.")
            ).getId();
        }
//        //pageable 옵션, page 는 -1 해주어야 한다. 가계부 날짜 역순으로 정렬
//        PageRequest pageRequest = PageRequest.of(
//                queryDto.getPage() - 1,
//                queryDto.getDisplay(),
//                Sort.by("date").descending()
//        );
//        Page<RecordRequestDto> page;
        List<RecordRequestDto> list;
        //마지막 7일간 소비지출 합
        Long weekSum = getWeekCost(userId, category);
        //해당 월 소비지출 합
        Long monthSum = getMonthCost(userId, category);
        //해당 유저의 기간 검색
        if (category.equals("all")) {
            list = recordRepository.findAllByUserIdOrderByDateDesc(
                            userId)
                    .stream().map(RecordRequestDto::new).collect(Collectors.toCollection(ArrayList::new));
            return new RecordResponseDto(list, weekSum, monthSum);

        }
        //해당 유저의 기간 및 카테고리 검색
        list = recordRepository.findAllByUserIdAndCategoryOrderByDateDesc(
                        userId,
                        category)
                .stream().map(RecordRequestDto::new).collect(Collectors.toCollection(ArrayList::new));

        return new RecordResponseDto(list, weekSum, monthSum);
    }

    //가계부 저장
    public Record saveRecord(RecordRequestDto requestDto, User user) {
        return recordRepository.save(new Record(requestDto, user));
    }

    //가계부 수정
    @Transactional
    public void editRecord(RecordRequestDto requestDto) {
        Record record = loadRecord(requestDto.getRecordId());
        record.updateRecord(requestDto);
    }

    //가계부 삭제
    public void deleteRecord(Long recordId) {
        Record record = loadRecord(recordId);
        recordRepository.delete(record);
    }

    //가계부 조회
    public Record loadRecord(Long recordId) {
        return recordRepository.findById(recordId).orElseThrow(
                () -> new CustomErrorException("게시물이 존재하지 않습니다.")
        );
    }

    //마지막 주 총 소비
    private Long getWeekCost(Long userId, String category) {
        Long weekSum = 0L;
        //모든 카테고리 일주일간 가계부 조회
        if (category.equals("all")) {
            List<Record> weekData = recordRepository.findAllByUserIdAndDateBetween(
                    userId,
                    LocalDate.now().minusDays(7),
                    LocalDate.now().plusDays(1));
            for (Record record : weekData) {
                weekSum += record.getCost();
            }
        } else {
            //카테고리 별 일주일간 가계부 조회
            List<Record> weekData = recordRepository.findAllByUserIdAndCategoryAndDateBetween(
                    userId,
                    category,
                    LocalDate.now().minusDays(7),
                    LocalDate.now().plusDays(1)
            );
            for (Record record : weekData) {
                weekSum += record.getCost();
            }
        }
        return weekSum;
    }

    //이번 달 총 소비
    private Long getMonthCost(Long userId, String category) {
        Long monthSum = 0L;
        //모든 카테고리 한달간 가계부 조회
        if (category.equals("all")) {
            List<Record> monthData = recordRepository.findAllByUserIdAndDateBetween(
                    userId,
                    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                    LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            );
            //지출 합
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        } else {
            //카테고리 별 한달간 가계부 조회
            List<Record> monthData = recordRepository.findAllByUserIdAndCategoryAndDateBetween(
                    userId,
                    category,
                    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                    LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            );
            //지출 합
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        }

        return monthSum;
    }
}
