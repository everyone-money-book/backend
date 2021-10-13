package com.team14.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameCheckRequestDto {

    @NotBlank(message = "유저네임은 필수값입니다.")
    @Length(min = 3, max = 10, message = "유저네임은 3~10자로 입력하셔야 합니다.")
    private String username;

}
