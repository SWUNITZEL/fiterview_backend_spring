package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.domain.SchoolRecord;
import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.domain.enums.Gender;
import com.swunitzel.fiterview.dto.JoinDto;
import com.swunitzel.fiterview.dto.TokenPairsDto;
import com.swunitzel.fiterview.dto.UserDto;
import com.swunitzel.fiterview.jwt.JWTUtil;
import com.swunitzel.fiterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final SchoolRecordService schoolRecordService;

    // 회원가입
    public void join(JoinDto joinDTO) {
        if (userRepository.existsByEmail(joinDTO.getEmail())) {
            throw new IllegalArgumentException("email already exist");
        }

        // 비밀번호 인코딩
        joinDTO.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        User user = new User(joinDTO);

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
    public TokenPairsDto reissueToken(String refresh) {

        User user = userRepository.findByEmail(jwtUtil.getEmail(refresh));
        if (user == null || !user.getRefresh().equals(refresh)) {
            System.out.println(user == null);
            System.out.println(user.getRefresh().equals(refresh));
            throw new RuntimeException();
        }

        String newAccessToken = jwtUtil.createJwt("access", jwtUtil.getEmail(refresh));

        return new TokenPairsDto(newAccessToken, refresh);

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
