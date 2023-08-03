package com.sopterm.makeawish.domain.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
import java.util.Objects;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoTokenManager {

    @Value("${social.kakao-url}")
    private String kakaoUrl;
    @Value("${social.client-id}")
    private String clientId;
    @Value("${social.redirect-uri}")
    private String redirectUri;

    public String getAccessTokenByCode(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
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
        if(Objects.isNull(responseBody)) throw new IllegalArgumentException(INVALID_CODE.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public StringBuilder getKakaoInfo(String socialToken) {
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

    public AuthSignInRequestDTO getAccessTokenByCode(JsonElement element) {
        String email = validateEmail(element.getAsJsonObject().get("kakao_account"));
        String name = element.getAsJsonObject().get("properties")
                .getAsJsonObject().get("nickname").getAsString();
        String socialId = element.getAsJsonObject().get("id").getAsString();
        return new AuthSignInRequestDTO(email, SocialType.KAKAO, socialId, name, LocalDateTime.now());
    }

    private String validateEmail(JsonElement element) {
        boolean ifEmailIsNotAgreed = element.getAsJsonObject().get("email_needs_agreement").getAsBoolean();
        if (ifEmailIsNotAgreed) throw new IllegalArgumentException(DISAGREE_KAKAO_EMAIL.getMessage());
        return element.getAsJsonObject().get("email").getAsString();
    }
}
