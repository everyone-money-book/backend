package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RecordRequestDto {
    private int year;
    private int month;
    private int date;
    private String contents;
    private String category;
    private Long cost;
    private Long userId;
    private Long recordId;
    private int page;
    private int display;
}
