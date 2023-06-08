package com.sopterm.makeawish.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sopterm.makeawish.config.jwt.JwtTokenProvider;
import com.sopterm.makeawish.config.jwt.UserAuthentication;
import com.sopterm.makeawish.domain.user.SocialType;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDto;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDto;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${social.kakao-url}")
    private String kakaoUrl;
    @Value("${social.client-id}")
    private String CLIENT_ID;
    @Value("${social.redirect-uri}")
    private String REDIRECT_URI;

    @Override
    public AuthSignInResponseDto socialLogin(String code) {
        String kakaoAccessToken = null;
        try {
            kakaoAccessToken = getAccessToken(code);
        } catch (JsonProcessingException j) {
            throw new IllegalArgumentException(CODE_PARSE_ERROR.getMessage());
        }
        StringBuilder kakaoInfo = getKakaoInfo(kakaoAccessToken);
        JsonElement element = JsonParser.parseString(kakaoInfo.toString());
        validateHasEmail(element);
        Long userId = getAccessToken(element);
        Authentication authentication = new UserAuthentication(userId, null, null);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        return new AuthSignInResponseDto(accessToken, refreshToken);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private StringBuilder getKakaoInfo(String socialToken) {
        try {
            URL url = new URL(kakaoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + socialToken);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(FAILED_VALIDATE_KAKAO_LOGIN.getMessage());
        }
    }
    private void validateHasEmail(JsonElement element) {
        boolean hasEmail = element
                .getAsJsonObject().get("kakao_account")
                .getAsJsonObject().get("has_email")
                .getAsBoolean();
        if (!hasEmail) {
            throw new IllegalArgumentException(DISAGREE_KAKAO_EMAIL.getMessage());
        }
    }
    private Long getAccessToken(JsonElement element) {
        String email = element
                .getAsJsonObject().get("kakao_account")
                .getAsJsonObject().get("email")
                .getAsString();
        String name = element
                .getAsJsonObject().get("properties")
                .getAsJsonObject().get("nickname")
                .getAsString();
        String socialId = element
                .getAsJsonObject().get("id")
                .getAsString();
        return issueAccessToken(new AuthSignInRequestDto(email, SocialType.KAKAO, socialId, name, LocalDateTime.now()));
    }

    private Long issueAccessToken(AuthSignInRequestDto request) {
        User user = userRepository.findBySocialId(request.socialId())
                .orElseGet(() -> signup(request));
        return user.getId();
    }
    private User signup(AuthSignInRequestDto request) {
        return userRepository.save(new User(request));
    }
}
