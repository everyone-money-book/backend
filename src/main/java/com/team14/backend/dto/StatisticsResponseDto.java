package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class StatisticsResponseDto {
    String username;
    private Map<String,Long> records;
    private Long leftMoney;
}
