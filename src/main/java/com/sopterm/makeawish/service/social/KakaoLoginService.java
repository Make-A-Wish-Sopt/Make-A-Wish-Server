package com.sopterm.makeawish.service.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;
import com.sopterm.makeawish.domain.user.InternalTokenManager;
import com.sopterm.makeawish.domain.user.KakaoTokenManager;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.auth.AuthSignInRequestDTO;
import com.sopterm.makeawish.dto.auth.AuthSignInResponseDTO;
import com.sopterm.makeawish.external.SlackClient;
import com.sopterm.makeawish.repository.UserRepository;
import com.sopterm.makeawish.service.SocialLoginService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    private final UserRepository userRepository;
    private final InternalTokenManager tokenManager;
    private final KakaoTokenManager kakaoTokenManager;
    private final SlackClient slackClient;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    @Override
    public AuthSignInResponseDTO socialLogin(String code, String redirectUri) {
        String kakaoAccessToken = null;
        try {
            kakaoAccessToken = kakaoTokenManager.getAccessTokenByCode(code, redirectUri);
        } catch (JsonProcessingException j) {
            throw new IllegalArgumentException(CODE_PARSE_ERROR.getMessage());
        }
        val kakaoInfo = kakaoTokenManager.getKakaoInfo(kakaoAccessToken);
        val element = JsonParser.parseString(kakaoInfo.toString());
        val user = issueAccessToken(kakaoTokenManager.getAccessTokenByCode(element));
        val accessToken = tokenManager.createAuthAccessToken(user.getId());
        val refreshToken = tokenManager.createAuthRefreshToken(user.getId());
        user.updateRefreshToken(refreshToken);
        return new AuthSignInResponseDTO(accessToken,refreshToken,user.getNickname());
    }

    private User issueAccessToken(AuthSignInRequestDTO request) {
        return userRepository.findBySocialId(request.socialId())
                .orElseGet(() -> signup(request));
    }
    private User signup(AuthSignInRequestDTO request) {

        try {
            if (Objects.equals(activeProfile, "dev")) {
                val slackRequest = createSlackRequest(request.nickname(), request.email(), request.createdAt(), String.valueOf(request.socialType()));
                slackClient.postMessage(slackRequest.toString());
            }
        } catch (RuntimeException ex) {
            log.error("슬랙 요청이 실패했습니다 : " + ex.getMessage());
        }
        return userRepository.save(new User(request));
    }

    private JsonNode createSlackRequest(String nickname, String email, LocalDateTime createdAt, String socialType) {
        val rootNode = jsonMapper.createObjectNode();
        rootNode.put("text", "새로운 유저가 가입했어요!");
        val blocks = jsonMapper.createArrayNode();

        val textField = jsonMapper.createObjectNode();
        textField.put("type", "section");
        textField.set("text", createTextFieldNode("새로운 유저가 가입했어요!"));

        val contentNode = jsonMapper.createObjectNode();
        contentNode.put("type", "section");
        val fields = jsonMapper.createArrayNode();
        fields.add(createTextFieldNode("*이름:*\n" + nickname));
        fields.add(createTextFieldNode("*이메알:*\n"+ email));
        fields.add(createTextFieldNode("*가입 시간:*\n" + createdAt));
        fields.add(createTextFieldNode("*소셜:*\n" + socialType));
        contentNode.set("fields", fields);

        blocks.add(textField);
        blocks.add(contentNode);
        rootNode.set("blocks", blocks);
        return rootNode;
    }

    private JsonNode createTextFieldNode (String text) {
        val textField = jsonMapper.createObjectNode();
        textField.put("type", "mrkdwn");
        textField.put("text", text);
        return textField;
    }

}
