package com.team14.backend.service;

import com.team14.backend.dto.ResponseDto;
import com.team14.backend.dto.StatisticsRequestDto;
import com.team14.backend.dto.StatisticsResponseDto;
import com.team14.backend.model.Record;
import com.team14.backend.model.User;
import com.team14.backend.repository.StatisticsRepository;
import com.team14.backend.security.UserDetailsImpl;
import jdk.vm.ci.meta.Local;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;


    public ResponseDto getMyPageInfo(StatisticsRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        //해당 유저의 모든 레코드 다 가져오기
        LocalDate startDate = LocalDate.parse(requestDto.getStartDate());//"2019-01-10" ->2019-01-10
        LocalDate endDate = LocalDate.parse(requestDto.getEndDate());
        List<Record> recordList = statisticsRepository.findAllByUserAndDateBetween(
                user,
                startDate,
                endDate
        );
        Map<String,Long> categoryCost = new HashMap<>();

        //카테고리별 합계구하기, 전체합계에 더하기
        Long sumCost = 0L;
        for (Record record:recordList){
            String category = record.getCategory();
            Long cost = record.getCost();

            sumCost += cost;
            categoryCost.put(category, categoryCost.getOrDefault(category,0L)+cost);
        }

        //남은 돈 구하기
        Long leftMoney = -1L;
        if(user.getSalary() != null){
            leftMoney = user.getSalary() - sumCost;
        }
        //현재유저
        String username = user.getUsername();

        StatisticsResponseDto responseDto = new StatisticsResponseDto(username,categoryCost,leftMoney);
        return new ResponseDto("success","",responseDto);
    }
}
