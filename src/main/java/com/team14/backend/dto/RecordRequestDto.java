package com.team14.backend.dto;

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
    private String userId;
    private Long recordId;
    private int page;
    private int display;
}
