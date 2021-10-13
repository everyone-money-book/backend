package com.team14.backend.model;


import com.team14.backend.dto.RecordRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Record extends Timestamped{
    @Id
    @GeneratedValue
    @Column(name = "RECORD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(nullable = false)
    private Long cost;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    public Record(RecordRequestDto requestDto, User user) {
        this.user = user;
        this.cost = requestDto.getCost();
        this.contents = requestDto.getContents();
        this.category = requestDto.getCategory();
        this.date = requestDto.getDate();
    }

    public void updateRecord(RecordRequestDto requestDto) {
        this.cost = requestDto.getCost();
        this.contents = requestDto.getContents();
        this.category = requestDto.getCategory();
        this.date = requestDto.getDate();
    }
}
