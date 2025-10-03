package com.swunitzel.fiterview.dto;

import com.swunitzel.fiterview.domain.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinDto {
    private String email;

    private String name;

    private LocalDate birth;

    private Gender gender;

    private String promotion_code;
}
