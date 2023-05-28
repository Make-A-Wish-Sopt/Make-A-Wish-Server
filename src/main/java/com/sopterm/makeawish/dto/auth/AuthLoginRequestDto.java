package com.sopterm.makeawish.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthLoginRequestDto(
        @NonNull
        String accessToken,
        @NotNull
        String nickname,
        @NotNull
        String email

) {
    public static AuthLoginRequestDto from(String accessToken, String nickname, String email) {
        return AuthLoginRequestDto.builder()
                .accessToken(accessToken)
                .nickname(nickname)
                .email(email)
                .build();
    }
}
