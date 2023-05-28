package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthLoginResponseDto(
        @NonNull
        String accessToken
) {
    public static AuthLoginResponseDto from (String accessToken) {
        return AuthLoginResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
