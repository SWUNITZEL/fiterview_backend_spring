package com.swunitzel.fiterview.oauth;

import com.swunitzel.fiterview.apiPayload.code.status.ErrorStatus;
import com.swunitzel.fiterview.apiPayload.exception.handler.AuthHandler;
import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 클래스
 */
@Getter
@Slf4j
public class OAuthAttributes {
    private final String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        log.info("Extracting OAuthAttributes for socialType: {}", provider);
        log.info("attributes :: {}", attributes);
        if (provider.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        } else{
            throw new AuthHandler(ErrorStatus._KAKAO_OAUTH_PARSING_ERROR);
        }
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }


    public User toEntity(String provider, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .email(oAuth2UserInfo.getUserEmail())
                .name(oAuth2UserInfo.getNickname())
                .role(Role.GUEST)
                .provider(provider)
                .providerId(oAuth2UserInfo.getId())
                .build();
    }
}
