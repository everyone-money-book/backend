package com.team14.backend.dto;

import com.team14.backend.model.Record;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RecordDto {
    private Long recordId;
    private String category;
    private Long cost;
    private String contents;
    private LocalDate date;

    public RecordDto(Record record) {
        this.recordId = record.getId();
        this.category = record.getCategory();
        this.cost = record.getCost();
        this.contents = record.getContents();
        this.date = LocalDate.from(record.getDate());
    }
}
