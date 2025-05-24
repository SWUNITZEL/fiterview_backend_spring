package com.swunitzel.fiterview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPairsDto {
    private String accessToken;
    private String refreshToken;
}