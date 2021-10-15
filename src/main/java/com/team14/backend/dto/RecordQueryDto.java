package com.team14.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RecordQueryDto {
    private String userId;
    private String category;
    private LocalDate date;
}
