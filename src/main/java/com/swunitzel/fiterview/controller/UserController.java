package com.swunitzel.fiterview.controller;

import com.swunitzel.fiterview.dto.JoinDto;
import com.swunitzel.fiterview.dto.TokenPairsDto;
import com.swunitzel.fiterview.dto.UserDto;
import com.swunitzel.fiterview.jwt.CustomUserDetails;
import com.swunitzel.fiterview.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

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

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String refresh) {

        if (refresh == null || !refresh.startsWith("Bearer ")) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        refresh = refresh.substring(7);

        try{
            if (userService.validateRefreshToken(refresh)) {

                TokenPairsDto tokenPairsDto = userService.reissueToken(refresh);
                return ResponseEntity.ok(tokenPairsDto);
            } else {
                return new ResponseEntity<>("invalid refresh token", HttpStatus.UNAUTHORIZED);
            }
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e){
            return new ResponseEntity<>("user is null", HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/navigation_data")
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            UserDto userDto = userService.getUserData(userDetails.getUsername());
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("user is null", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }


}
