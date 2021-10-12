package com.team14.backend.service;

import com.team14.backend.dto.RecordDto;
import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.RecordResponseDto;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Record;
import com.team14.backend.repository.RecordRepository;
import com.team14.backend.repository.UserRepository;
import com.team14.backend.security.UserDetailsImpl;
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

    public ResponseDto getAllRecords(RecordRequestDto requestDto, UserDetailsImpl userDetails) {
        Long userId;
        if (requestDto.getUserId().equals("")) {
            userId = userDetails.getUser().getId();
        }else {
            userId = userRepository.findByUsername(requestDto.getUserId()).orElseThrow(
                    () -> new CustomErrorException("해당 유저 정보가 존재하지 않습니다.")
            ).getId();
        }
        PageRequest pageRequest = PageRequest.of(
                requestDto.getPage() - 1,
                requestDto.getDisplay(),
                Sort.by("date").descending()
        );
        Page<RecordDto> page;
        //마지막 7일간 소비지출 합
        Long weekSum = getWeekCost(userId, requestDto.getCategory());
        //해당 월 소비지출 합
        Long monthSum = getMonthCost(userId, requestDto.getCategory());
        //카테고리 별 검색
        if (requestDto.getCategory().equals("all")) {
            page = recordRepository.findAllByUserIdAndDateBefore(userId, requestDto.getDate(), pageRequest).map(RecordDto::new);
            RecordResponseDto responseDto = new RecordResponseDto(page, weekSum, monthSum);
            return new ResponseDto("success", "", responseDto);
        }
        page = recordRepository.findAllByUserIdAndCategory(
                        userId,
                        requestDto.getCategory(),
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

    public ResponseDto saveRecord(RecordRequestDto requestDto, UserDetailsImpl userDetails) {
        recordRepository.save(new Record(requestDto, userDetails));
        return new ResponseDto("success", "성공적으로 저장하였습니다.", "");
    }

    @Transactional
    public ResponseDto editRecord(RecordRequestDto requestDto, UserDetailsImpl userDetails) {
        Record record = recordRepository.findById(requestDto.getRecordId()).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );

        if (!record.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomErrorException("로그인 정보와 게시글의 유저 정보와 일치하지 않습니다.");
        }

        record.updateRecord(requestDto);
        return new ResponseDto("success", "성공적으로 수정되었습니다.", "");
    }

    public ResponseDto deleteRecord(RecordRequestDto requestDto, UserDetailsImpl userDetails) {
        Record record = recordRepository.findById(requestDto.getRecordId()).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );

        if (!record.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomErrorException("로그인 정보와 게시글의 유저 정보와 일치하지 않습니다.");
        }

        recordRepository.delete(record);
        return new ResponseDto("success", "성공적으로 삭제되었습니다.", "");
    }
}
