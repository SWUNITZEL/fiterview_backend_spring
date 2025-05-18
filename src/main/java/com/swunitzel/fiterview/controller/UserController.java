package com.swunitzel.fiterview.controller;

import com.swunitzel.fiterview.dto.JoinDto;
import com.swunitzel.fiterview.jwt.JWTUtil;
import com.swunitzel.fiterview.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto) {
        try {
            userService.join(joinDto);
            return ResponseEntity.ok("ok");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }

}
