package com.swunitzel.fiterview.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.dto.LoginDto;
import com.swunitzel.fiterview.dto.TokenPairsDto;
import com.swunitzel.fiterview.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

            //클라이언트 요청에서 username, password 추출
            String email = loginDto.getEmail();
            String password = loginDto.getPassword();

            //스프링 시큐리티에서 username과 password를 검증
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);
        } catch (
                IOException e) {
            throw new RuntimeException("Failed to read login request", e);
        }
    }

    //로그인 성공시 실행하는 메소드: JWT를 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        //토큰 생성
        String access = jwtUtil.createJwt("access", email);
        String refresh = jwtUtil.createJwt("refresh", email);

        //응답 설정
        response.setHeader("access", access);
        response.setHeader("refresh", refresh);
        if (saveRefresh(email, refresh)){
            try{
                // DTO 생성 및 JSON 직렬화
                TokenPairsDto tokenPair = new TokenPairsDto(access, refresh);
                String jsonResponse = new ObjectMapper().writeValueAsString(tokenPair);

                response.getWriter().write(jsonResponse);
                response.setStatus(HttpStatus.OK.value());
            } catch (IOException e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
        }
        else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }

    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

    // refresh 저장
    public boolean saveRefresh(String email, String refresh) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        user.setRefresh(refresh);
        userRepository.save(user);

        return true;

    }

}