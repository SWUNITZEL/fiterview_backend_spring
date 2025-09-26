package com.swunitzel.fiterview.dto;

import com.swunitzel.fiterview.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TokenPairsDto {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Oauth2ResponseDto {
        private String accessToken;
        private String refreshToken;
        private String email;
        private Role role;
    }

}