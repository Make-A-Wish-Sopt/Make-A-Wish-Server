package com.sopterm.makeawish.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.sopterm.makeawish.config.jwt.JwtTokenProvider;
import com.sopterm.makeawish.config.jwt.UserAuthentication;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.auth.AuthGetTokenResponseDto;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${social.oauth.kakao.redirect.user-info-uri}")
    private String kakaoUserInfoUriAuth;

    @Transactional
    public AuthSignInResponseDto signIn(AuthSignInRequestDto dto, String socialAccessToken) {
        String socialId = String.valueOf(getKakaoUserData(socialAccessToken));

        if (userRepository.existsBySocialId(socialId)) {
            boolean isRegistered = true;
            Long userId = userRepository.findIdBySocialId(socialId);
            Authentication authentication = new UserAuthentication(userId, null, null);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            return new AuthSignInResponseDto(accessToken, refreshToken, isRegistered);
        }

        boolean isRegistered = false;

        User user = User.
                builder()
                .socialType(SocialType.KAKAO)
                .socialId(socialId)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        User newUser = userRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, socialId);
        Authentication authentication = new UserAuthentication(newUser.getId(), null, null);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        newUser.updateRefreshToken(refreshToken);
        userRepository.save(newUser);

        return new AuthSignInResponseDto(accessToken, refreshToken, isRegistered);
    }

    private Long getKakaoUserData(String socialAccessToken)  {
        RestTemplate restTemplate = new RestTemplate();

        String kakaoUrl = kakaoUserInfoUriAuth;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", socialAccessToken);

            HttpEntity<JsonArray> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<Object> responseData = restTemplate.exchange(
                    kakaoUrl,
                    HttpMethod.GET,
                    httpEntity,
                    Object.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(responseData.getBody(), Map.class);
            Long id = (Long) map.get("id");
            return id;
        } catch (HttpClientErrorException e ) {
            throw e;
        }
    }

    public AuthGetTokenResponseDto getToken(Long userId) {
        Authentication authentication = new UserAuthentication(userId, null, null);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        user.updateRefreshToken(refreshToken);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        AuthGetTokenResponseDto responseDto = AuthGetTokenResponseDto.builder().
                refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
        return responseDto;
    }
}
