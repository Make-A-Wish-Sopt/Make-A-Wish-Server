package com.sopterm.makeawish.service;

import com.sopterm.makeawish.config.jwt.JwtTokenProvider;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.social.KakaoLoginService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;

@Service
@Component
@RequiredArgsConstructor
public class AuthService {
    private final Map<SocialType, SocialLoginService> socialLogins = new EnumMap<>(SocialType.class);
    private final KakaoLoginService kakaoLoginService;


    @Transactional
    public AuthSignInResponseDto socialLogin(String social, String code){
        SocialType socialType = SocialType.from(social);
        SocialLoginService socialLoginService = socialLogins.get(socialType);
        return socialLoginService.socialLogin(code);
    }
    @PostConstruct
    private void init() {
        socialLogins.put(SocialType.KAKAO, kakaoLoginService);
    }
}
