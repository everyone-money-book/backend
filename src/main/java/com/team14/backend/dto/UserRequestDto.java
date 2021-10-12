package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private String username;
    private String password;
    private String sex;
    private Long age;
    private String job;
    private Long salary;
    private boolean admin = false;
    private String adminToken = "";

}
