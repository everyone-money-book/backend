package com.team14.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class RecordResponseDto {
    private Page<RecordDto> records;
    private Long weekCost;
    private Long totalCost;

    public RecordResponseDto(Page<RecordDto> page, Long weekSum, Long monthSum) {
        this.records = page;
        this.weekCost = weekSum;
        this.totalCost = monthSum;
    }
}
