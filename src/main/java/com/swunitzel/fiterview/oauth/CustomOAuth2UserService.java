package com.swunitzel.fiterview.oauth;

import com.swunitzel.fiterview.converter.UserConverter;
import com.swunitzel.fiterview.domain.User;
import com.swunitzel.fiterview.domain.enums.Role;
import com.swunitzel.fiterview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String KAKAO = "kakao";

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String provider = getProvider(registrationId);

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json (유저 정보들)

        // socialType에 따라 유저정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);

        User createMember = getUser(extractAttributes, provider);

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createMember.getRole().toString())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createMember.getEmail(),
                createMember.getRole()
        );
    }


    private User getUser(OAuthAttributes attributes, String provider) {

        User user = userRepository.findByEmail(attributes.getOAuth2UserInfo().getUserEmail());
        if (user == null) {
            user = createNewUser(attributes);
        }

        return user;
    }

    private User createNewUser(OAuthAttributes attributes) {
        User newUser = UserConverter.toUser(
                attributes.getOAuth2UserInfo().getUserEmail(),
                attributes.getOAuth2UserInfo().getNickname(),
                attributes.getOAuth2UserInfo().getId(),
                passwordEncoder,
                Role.GUEST
        );
        return userRepository.save(newUser);
    }

    private String getProvider(String registrationId) {
        if (KAKAO.equals(registrationId.toUpperCase())) {
            return "kakao";
        }

        return "kakao";
    }
}