package com.sopterm.makeawish.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AuthConfig {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${social.oauth.kakao.redirect.url}")
    private String kakaoRedirectUrl;
    @Value("${social.oauth.kakao.redirect.user-info-uri}")
    private String kakaoUserInfoUriAuth;
    @Value("${social.oauth.kakao.client.id}")
    private String kakaoClientId;
    @Value("${social.oauth.kakao.client.secret}")
    private String kakaoClientSecretId;
}
