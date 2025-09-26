package com.swunitzel.fiterview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
public class UserDto {

    private String name;

    private String email;

    private String profileImg;

    private String documentId;

}
