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
        log.info("로그인 요청 진입");
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해  DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser() 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보냄
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후, registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId :: {} ", registrationId);
        String provider = getProvider(registrationId);
        log.info("socialType :: {}", provider);
        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json (유저 정보들)

        log.info("registrationId = {}", registrationId);
        log.info("userNameAttributeName = {}", userNameAttributeName);

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

    /**
     * TODO: SocialType,
     * SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾아 반환하는 메소드
     * 만약 찾은 회원이 있다면, 그대로 반환하고 없다면 save를 통하여 회원을 저장한다.
     *
     * @param attributes
     * @param provider
     * @return
     */
    private User getUser(OAuthAttributes attributes, String provider) {

        User user = userRepository.findByEmail(attributes.getOAuth2UserInfo().getUserEmail());
        if (user == null) {
            System.out.println("user == null");
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