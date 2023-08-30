package com.sopterm.makeawish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.common.message.ErrorMessage;
import com.sopterm.makeawish.domain.user.InternalTokenManager;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDTO;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDTO;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.social.KakaoLoginService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.*;
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
    public AuthSignInResponseDTO socialLogin(String social, String code, String redirectUri) throws JsonProcessingException {
        val socialType = SocialType.from(social);
        val socialLoginService = socialLogins.get(socialType);
        return socialLoginService.socialLogin(code, redirectUri);
    }

    @Transactional
    public AuthGetTokenResponseDTO getToken(Long userId) {
        val user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.INVALID_USER.getMessage()));
        val refreshToken = tokenManager.createAuthRefreshToken(userId);
        user.updateRefreshToken(refreshToken);
        val accessToken = tokenManager.createAuthAccessToken(userId);
        return AuthGetTokenResponseDTO.builder().
                refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    @PostConstruct
    private void init() {
        socialLogins.put(SocialType.KAKAO, kakaoLoginService);
    }
}
