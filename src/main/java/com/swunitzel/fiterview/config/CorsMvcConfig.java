package com.swunitzel.fiterview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",           // 개발용
                        "https://www.fiterview.site",      // 프론트엔드 도메인
                        "https://fiterview.site",           // www 없는 도메인도 추가
                        "https://localhost:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);  // 쿠키/인증 정보 허용
    }
}
