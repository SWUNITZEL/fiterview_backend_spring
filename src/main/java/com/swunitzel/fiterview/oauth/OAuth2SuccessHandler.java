package com.swunitzel.fiterview.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.AuthHandler;
import com.swunitzel.fiterview.domain.enums.Role;
import com.swunitzel.fiterview.dto.TokenDto;
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

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
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

            System.out.println("accessToken: " + accessToken);
            System.out.println("refreshToken: " + refreshToken);

            TokenDto.Oauth2ResponseDto oauth2ResponseDto;

            // 최초 OAuth 로그인 시 Guest
            if(oAuth2User.getRole() == Role.GUEST){
                oauth2ResponseDto = TokenDto.Oauth2ResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .email(email)
                        .role(Role.GUEST)
                        .build();

            } else {
                oauth2ResponseDto = TokenDto.Oauth2ResponseDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .email(email)
                        .role(Role.USER)
                        .build();
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(oauth2ResponseDto));

            userService.updateRefresh(oAuth2User.getEmail(), refreshToken);

        } catch(Exception e){
            throw new AuthHandler(ErrorStatus._KAKAO_OAUTH_SERVER_ERROR);
        }
    }

}