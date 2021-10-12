package com.team14.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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

    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
