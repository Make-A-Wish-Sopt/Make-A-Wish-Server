package com.sopterm.makeawish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.common.message.ErrorMessage;
import com.sopterm.makeawish.domain.user.InternalTokenManager;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.social.KakaoLoginService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;

@Service
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final Map<SocialType, SocialLoginService> socialLogins = new EnumMap<>(SocialType.class);
    private final KakaoLoginService kakaoLoginService;
    private final InternalTokenManager tokenManager;
    private final UserRepository userRepository;

    @Transactional
    public AuthSignInResponseDto socialLogin(String social, String code) throws JsonProcessingException {
        SocialType socialType = SocialType.from(social);
        SocialLoginService socialLoginService = socialLogins.get(socialType);
        return socialLoginService.socialLogin(code);
    }

    @Transactional
    public AuthGetTokenResponseDto getToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.INVALID_USER.getMessage()));
        String refreshToken = tokenManager.createAuthRefreshToken(userId);
        user.updateRefreshToken(refreshToken);
화        String accessToken = tokenManager.createAuthAccessToken(userId);
        return AuthGetTokenResponseDto.builder().
                refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    @PostConstruct
    private void init() {
        socialLogins.put(SocialType.KAKAO, kakaoLoginService);
    }
}
