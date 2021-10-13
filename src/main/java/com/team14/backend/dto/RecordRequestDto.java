package com.team14.backend.dto;

import com.team14.backend.model.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RecordRequestDto {
    private LocalDate date;
    private String contents;
    private String category;
    private Long cost;
    private Long recordId;

    public RecordRequestDto(Record record) {
        this.date = record.getDate();
        this.contents = record.getContents();
        this.category = record.getCategory();
        this.cost = record.getCost();
        this.recordId = record.getId();
    }
}
