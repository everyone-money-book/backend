package com.team14.backend.dto;

import com.team14.backend.model.Record;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecordDto {
    private final Long recordId;
    private final String category;
    private final Long cost;
    private final String contents;
    private final int year;
    private final int month;
    private final int date;

    public RecordDto(Record record) {
        this.recordId = record.getId();
        this.category = record.getCategory();
        this.cost = record.getCost();
        this.contents = record.getContents();
        LocalDateTime time = record.getDate();
        this.year = time.getYear();
        this.month = time.getMonthValue();
        this.date = time.getDayOfMonth();
    }
}
