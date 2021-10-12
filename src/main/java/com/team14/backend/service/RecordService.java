package com.team14.backend.service;

import com.team14.backend.dto.RecordDto;
import com.team14.backend.dto.RecordRequestDto;
import com.team14.backend.dto.RecordResponseDto;
import com.team14.backend.dto.ResponseDto;
import com.team14.backend.exception.CustomErrorException;
import com.team14.backend.model.Record;
import com.team14.backend.repository.RecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public ResponseDto getAllRecords(RecordRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), requestDto.getDisplay(), Sort.by("date").descending());

        Page<RecordDto> page;
        //마지막 7일간 소비지출 합
        Long weekSum = getWeekCost(requestDto.getCategory());
        //마지막 30일간 소비지출 합
        Long monthSum = getMonthCost(requestDto.getCategory());
        if (requestDto.getCategory().equals("all")) {
            page = recordRepository.findAll(pageRequest).map(RecordDto::new);
            RecordResponseDto responseDto = new RecordResponseDto(page, weekSum, monthSum);
            return new ResponseDto("success", "", responseDto);
        }
        page = recordRepository.findAllByCategory(requestDto.getCategory(), pageRequest).map(RecordDto::new);

        RecordResponseDto responseDto = new RecordResponseDto(page, weekSum, monthSum);
        return new ResponseDto("success", "", responseDto);
    }

    private Long getWeekCost(String category) {
        Long weekSum = 0L;
        if(category.equals("all")){
            List<Record> weekData = recordRepository.findAllByDateBetween(LocalDate.now().minusDays(7).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            for (Record record : weekData) {
                weekSum += record.getCost();
            }
        }else {
            List<Record> weekData = recordRepository.findAllByCategoryAndDateBetween(category, LocalDate.now().minusDays(7).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            for (Record record : weekData) {
                weekSum += record.getCost();
            }
        }

        return weekSum;
    }

    private Long getMonthCost(String category) {
        Long monthSum = 0L;
        if(category.equals("all")) {
            List<Record> monthData = recordRepository.findAllByDateBetween(LocalDate.now().minusMonths(1).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        }else {
            List<Record> monthData = recordRepository.findAllByCategoryAndDateBetween(category, LocalDate.now().minusMonths(1).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            for (Record record : monthData) {
                monthSum += record.getCost();
            }
        }

        return monthSum;
    }

    public ResponseDto saveRecord(RecordRequestDto requestDto) {
        recordRepository.save(new Record(requestDto));
        return new ResponseDto("success", "성공적으로 저장하였습니다.", "");
    }

    @Transactional
    public ResponseDto editRecord(RecordRequestDto requestDto) {
        Record record = recordRepository.findById(requestDto.getRecordId()).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );

        record.updateRecord(requestDto);
        return new ResponseDto("success", "성공적으로 수정되었습니다.", "");
    }

    public ResponseDto deleteRecord(RecordRequestDto requestDto) {
        Record record = recordRepository.findById(requestDto.getRecordId()).orElseThrow(
                () -> new CustomErrorException("해당 기록이 존재하지 않습니다.")
        );

        recordRepository.delete(record);
        return new ResponseDto("success", "성공적으로 삭제되었습니다.", "");
    }
}
