package com.team14.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String sex;

    @Column
    private Long age;

    @Column
    private String job;

    @Column
    private Long salary;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Record> records;

    @OneToMany(mappedBy = "toUser")
    @JsonIgnore
    private List<Follow> followers;

    @OneToMany(mappedBy = "fromUser")
    @JsonIgnore
    private List<Follow> followings;

    @Column(unique = true)
    private Long kakaoId;
    
    //일반회원 회원가입
    public User(String username, String password, String sex, Long age, String job, Long salary, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
        this.job = job;
        this.salary = salary;
        this.role = role;
        this.kakaoId = null;
    }
    
    //카카오회원 회원가입
    public User(String username, String encodedPassword, UserRoleEnum role, Long kakaoId) {
        this.username = username;
        this.password = encodedPassword;
        this.role = role;
        this.kakaoId = kakaoId;
    }
    
    //회원정보 업데이트 메서드
    public void update(String sex, Long age, String job, Long salary) {
        this.sex = sex;
        this.age = age;
        this.job = job;
        this.salary = salary;
    }
}
