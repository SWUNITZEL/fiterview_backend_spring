package com.swunitzel.fiterview.dto;

import com.swunitzel.fiterview.domain.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class JoinDto {
    private String email;

    private String password;

    private String name;

    private LocalDate birth;

    private Gender gender;

    private String promotion_code;
}
