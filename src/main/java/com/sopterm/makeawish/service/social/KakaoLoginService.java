package com.sopterm.makeawish.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sopterm.makeawish.domain.user.InternalTokenManager;
import com.sopterm.makeawish.domain.user.KakaoTokenManager;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final UserRepository userRepository;
    private final InternalTokenManager tokenManager;
    private final KakaoTokenManager kakaoTokenManager;

    @Override
    public AuthSignInResponseDto socialLogin(String code) {
        String kakaoAccessToken = null;
        try {
            kakaoAccessToken = kakaoTokenManager.getAccessTokenByCode(code);
        } catch (JsonProcessingException j) {
            throw new IllegalArgumentException(CODE_PARSE_ERROR.getMessage());
        }
        StringBuilder kakaoInfo = kakaoTokenManager.getKakaoInfo(kakaoAccessToken);
        JsonElement element = JsonParser.parseString(kakaoInfo.toString());
        User user = issueAccessToken(kakaoTokenManager.getAccessTokenByCode(element));
        String accessToken = tokenManager.createAuthAccessToken(user.getId());
        String refreshToken = tokenManager.createAuthRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        return new AuthSignInResponseDto(accessToken,refreshToken);
    }

    private User issueAccessToken(AuthSignInRequestDto request) {
        return userRepository.findBySocialId(request.socialId())
                .orElseGet(() -> signup(request));
    }
    private User signup(AuthSignInRequestDto request) {
        return userRepository.save(new User(request));
    }
}
