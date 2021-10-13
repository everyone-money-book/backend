package com.team14.backend.service;

import com.team14.backend.dto.*;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.repository.RecordRepository;
import com.team14.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public ResponseDto getAllRecords(RecordQueryDto queryDto, User user) {
        Long userId;
        if (queryDto.getUserId().equals("")) {
            userId = user.getId();
        }else {
            userId = userRepository.findByUsername(queryDto.getUserId()).orElseThrow(
                    () -> new CustomErrorException("해당 유저 정보가 존재하지 않습니다.")
            ).getId();
        }
        PageRequest pageRequest = PageRequest.of(
                queryDto.getPage() - 1,
                queryDto.getDisplay(),
                Sort.by("date").descending()
        );
        Page<RecordDto> page;
        //마지막 7일간 소비지출 합
        Long weekSum = getWeekCost(userId, queryDto.getCategory());
        //해당 월 소비지출 합
        Long monthSum = getMonthCost(userId, queryDto.getCategory());
        //카테고리 별 검색
        if (queryDto.getCategory().equals("all")) {
            page = recordRepository.findAllByUserIdAndDateBefore(userId, queryDto.getDate(), pageRequest).map(RecordDto::new);
            RecordResponseDto responseDto = new RecordResponseDto(page, weekSum, monthSum);
            return new ResponseDto("success", "", responseDto);
        }
        page = recordRepository.findAllByUserIdAndCategory(
                        userId,
                        queryDto.getCategory(),
                        pageRequest)
                .map(RecordDto::new);

        RecordResponseDto responseDto = new RecordResponseDto(page, weekSum, monthSum);
        return new ResponseDto("success", "", responseDto);
    }

    private Long getWeekCost(Long userId, String category) {
        Long weekSum = 0L;
        if (category.equals("all")) {
            List<Record> weekData = recordRepository.findAllByUserIdAndDateBetween(
                    userId,
                    LocalDate.now().minusDays(7),
                    LocalDate.now().plusDays(1));
            for (Record record : weekData) {
                weekSum += record.getCost();
            }
        } else {
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

    private Long getMonthCost(Long userId, String category) {
        Long monthSum = 0L;
        if (category.equals("all")) {
            List<Record> monthData = recordRepository.findAllByUserIdAndDateBetween(
                    userId,
                    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth())
            );
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        } else {
            List<Record> monthData = recordRepository.findAllByUserIdAndCategoryAndDateBetween(
                    userId,
                    category,
                    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
                    LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth())
            );
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        }

        return monthSum;
    }

    public Record saveRecord(RecordRequestDto requestDto, User user) {
        return recordRepository.save(new Record(requestDto, user));
    }

    @Transactional
    public void editRecord(RecordRequestDto requestDto) {
        Record record = recordRepository.findById(requestDto.getRecordId()).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );
        record.updateRecord(requestDto);
    }

    public void deleteRecord(Long recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );
        recordRepository.delete(record);
    }

    public Record loadRecord(Long recordId) {
        return recordRepository.findById(recordId).orElseThrow(
                () -> new CustomErrorException("게시물이 존재하지 않습니다.")
        );
    }
}
