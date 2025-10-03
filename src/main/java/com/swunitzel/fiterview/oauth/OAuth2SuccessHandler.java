package com.swunitzel.fiterview.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.AuthHandler;
import com.swunitzel.fiterview.jwt.JWTUtil;
import com.swunitzel.fiterview.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        try{
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getEmail();

            // Access Token과 Refresh Token 생성
            String accessToken = jwtUtil.createJwt("access", email);
            String refreshToken = jwtUtil.createJwt("refresh",  email);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            userService.updateRefresh(oAuth2User.getEmail(), refreshToken);

            String targetUrl = UriComponentsBuilder.fromUriString("https://fiterview.site/auth/callback")
                    .queryParam("accessToken", accessToken)
                    .queryParam("refreshToken", refreshToken)
                    .queryParam("email", email)
                    .queryParam("role", oAuth2User.getRole().name())
                    .build()
                    .toUriString();


            response.sendRedirect(targetUrl);

        } catch(Exception e){
            throw new AuthHandler(ErrorStatus._KAKAO_OAUTH_SERVER_ERROR);
        }
    }

}