package com.swunitzel.fiterview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 모든 경로를 index.html로 포워딩. 단, /api로 시작하는 경로는 제외
        registry.addViewController("/{path:^(?!api|static|analysis|report).*$}")
                .setViewName("forward:/");
        // 루트 경로도 index.html로 포워딩
        registry.addViewController("/")
                .setViewName("forward:/");
    }
}

