package com.swunitzel.fiterview.domain;

import com.swunitzel.fiterview.domain.enums.Gender;
import com.swunitzel.fiterview.domain.enums.Role;
import com.swunitzel.fiterview.dto.JoinDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "user")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private Role role;

    private String provider;

    private String providerId;

    public  User(JoinDto joinDto, Role role) {
        this.email = joinDto.getEmail();
        this.password = joinDto.getPassword();
        this.name = joinDto.getName();
        this.birth = joinDto.getBirth();
        this.gender = joinDto.getGender();
        this.role = role;
        if (joinDto.getPromotion_code() != null){
            this.promotion_code = joinDto.getPromotion_code();;
        }

    }

    public void updateRefresh(String token){
        this.refresh = token;
    }

    public void updateUser(JoinDto joinDto, Role role) {
        this.gender = joinDto.getGender();
        this.promotion_code = joinDto.getPromotion_code();
        this.birth = joinDto.getBirth();
        this.name = joinDto.getName();
        this.role = role;
    }

}
