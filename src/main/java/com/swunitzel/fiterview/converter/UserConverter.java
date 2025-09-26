package com.swunitzel.fiterview.converter;

import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.domain.enums.Role;
import com.swunitzel.fiterview.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserConverter {
    public static User toUser(String email, String name, String password, PasswordEncoder passwordEncoder, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRole(role);
        return user;
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .profileImg(user.getProfileImg())
                .build();

    }
}
