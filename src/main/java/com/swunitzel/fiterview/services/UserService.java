package com.swunitzel.fiterview.services;

import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.dto.JoinDto;
import com.swunitzel.fiterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDto joinDTO) {

        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new IllegalArgumentException("email already exist");
        }

        User data = new User();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(data);
    }

}
