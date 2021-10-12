package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "유저네임은 필수값입니다.")
    @Length(min = 3, max = 10, message = "유저네임은 3~10자로 입력하셔야 합니다.")
    private String username;
    
    @NotBlank(message = "비밀번호는 필수값입니다.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z!@#$%^&*]{4,}$",
            message = "비밀번호는 최소 4자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)를 적어도 1개 이상 포함하고 특수문자도 쓸 수 있습니다")
    private String password;
    
    @NotBlank(message = "성별은 필수값입니다.")
    private String sex;
    
    @NotBlank(message = "나이는 필수값입니다.")
    private Long age;
    
    @NotBlank(message = "직업은 필수값입니다.")
    private String job;
    
    @NotBlank(message = "월급은 필수값입니다.")
    private Long salary;
    
    private boolean admin = false;
    private String adminToken = "";

}
