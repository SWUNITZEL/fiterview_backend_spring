package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.UserHandler;
import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.domain.enums.Role;
import com.swunitzel.fiterview.dto.JoinDto;
import com.swunitzel.fiterview.dto.TokenDto;
import com.swunitzel.fiterview.dto.UserDto;
import com.swunitzel.fiterview.jwt.JWTUtil;
import com.swunitzel.fiterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final SchoolRecordService schoolRecordService;

    @Transactional
    public void updateUser(JoinDto joinDto) {
        User user = userRepository.findByEmail(joinDto.getEmail());
        if (user == null) {
            throw new UserHandler(ErrorStatus._USER_NOT_FOUND);
        }

        user.updateUser(joinDto, Role.USER);
        userRepository.save(user);
    }

    public Boolean validateRefreshToken(String refresh) {
        jwtUtil.isExpired(refresh);
        if (!jwtUtil.getCategory(refresh).equals("refresh")) {

            throw new IllegalArgumentException("is not refresh token");
        }
        return true;
    }


    // refresh 토큰 갱신
    public TokenDto.TokenPairsDto reissueToken(String refresh) {

        User user = userRepository.findByEmail(jwtUtil.getEmail(refresh));
        if (user == null || !user.getRefresh().equals(refresh)) {
            System.out.println(user == null);
            System.out.println(user.getRefresh().equals(refresh));
            throw new RuntimeException();
        }

        String newAccessToken = jwtUtil.createJwt("access", jwtUtil.getEmail(refresh));

        return TokenDto.TokenPairsDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refresh)
                .build();

    }

    // refresh 토큰 저장
    @Transactional
    public void updateRefresh(String email, String refreshToken) {
        User user = userRepository.findByEmail(email);
        user.updateRefresh(refreshToken);
    }

    public UserDto getUserData(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException();
        }

        String schoolRecordId = schoolRecordService.getSchoolRecordId(email);

        return new UserDto(user.getName(), user.getEmail(), user.getProfileImg(), schoolRecordId  );
    }

}
