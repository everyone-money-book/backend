package com.team14.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecordResponseDto {
    private List<RecordRequestDto> records;
    private Long weekCost;
    private Long totalCost;

    public RecordResponseDto(List<RecordRequestDto> list, Long weekSum, Long monthSum) {
        this.records = list;
        this.weekCost = weekSum;
        this.totalCost = monthSum;
    }
}
