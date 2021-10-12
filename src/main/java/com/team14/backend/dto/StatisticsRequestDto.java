package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class StatisticsRequestDto {
    String startDate;
    String endDate;
}
