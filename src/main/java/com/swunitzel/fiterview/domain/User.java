package com.swunitzel.fiterview.domain;

import com.swunitzel.fiterview.domain.enums.Gender;
import com.swunitzel.fiterview.dto.JoinDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "user")
@Getter @Setter
@NoArgsConstructor
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

    private String profileImg;

    public  User(JoinDto joinDto) {
        this.email = joinDto.getEmail();
        this.password = joinDto.getPassword();
        this.name = joinDto.getName();
        this.birth = joinDto.getBirth();
        this.gender = joinDto.getGender();
        if (joinDto.getPromotion_code() != null){
            this.promotion_code = joinDto.getPromotion_code();;
        }

    }
}
