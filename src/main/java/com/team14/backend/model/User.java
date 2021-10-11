package com.team14.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private Long salary;

    @OneToMany(mappedBy = "user")
    private List<Record> records

    @OneToMany(mappedBy = "toUser")
    private List<User> follwers

    @OneToMany(mappedBy = "fromUser")
    private List<User> followings

    @Column(unique = true)
    private Long kakaoId;
    
    //일반회원 회원가입
    public User(String username, String password, String sex, Long age, String job, Long salary) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.job = job;
        this.salary = salary;
    }

}
