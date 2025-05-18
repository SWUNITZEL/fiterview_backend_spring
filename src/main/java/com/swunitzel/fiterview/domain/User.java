package com.swunitzel.fiterview.domain;

import com.swunitzel.fiterview.domain.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "user")
@Getter @Setter
public class User extends BaseEntity {
    @Id
    private String id;

    private String name;

    private String password;

    private String email;

    private LocalDate birth;

    private Gender gender;

    private String promotion_code;

    private String refresh;
}
