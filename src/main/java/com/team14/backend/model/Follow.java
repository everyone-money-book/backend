package com.team14.backend.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Follow extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name="FROM_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @JoinColumn(name="TO_USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

}
