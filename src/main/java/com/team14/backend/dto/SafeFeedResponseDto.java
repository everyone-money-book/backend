package com.team14.backend.dto;

import com.team14.backend.model.Record;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SafeFeedResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String username;

    private Long cost;

    private String contents;
    private String category;
    private LocalDate date;

    public SafeFeedResponseDto(Record record){
        this.createdAt = record.getCreatedAt();
        this.modifiedAt = record.getModifiedAt();
        this.username = record.getUser().getUsername();
        this.cost = record.getCost();
        this.contents = record.getContents();
        this.category = record.getCategory();
        this.date = record.getDate();
    }

}
