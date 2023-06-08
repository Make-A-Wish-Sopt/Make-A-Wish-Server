package com.sopterm.makeawish.dto.auth;

import com.sopterm.makeawish.domain.user.SocialType;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
public record AuthSignInRequestDto(
        @Email
        String email,
        @NonNull
        SocialType socialType,
        @NonNull
        String socialId,
        @NonNull
        String nickname,
        @NonNull
        LocalDateTime createdAt
){
        public static AuthSignInRequestDto to(String email, SocialType socialType, String socialId, String nickname, LocalDateTime createdAt) {
                return AuthSignInRequestDto.builder()
                        .email(email)
                        .socialType(socialType)
                        .nickname(nickname)
                        .socialId(socialId)
                        .createdAt(createdAt)
                        .build();
        }
}
