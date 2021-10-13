package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateReqeustDto {

    @NotBlank(message = "성별은 필수값입니다.")
    private String sex;
    
    @NotNull(message = "나이는 필수값입니다.")
    private Long age;
    
    @NotBlank(message = "직업은 필수값입니다.")
    private String job;
    
    @NotNull(message = "월급은 필수값입니다.")
    private Long salary;

}
