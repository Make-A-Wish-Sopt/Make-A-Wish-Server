package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthSignInResponseDto(
        @NonNull
        String accessToken,
        @NonNull
        String refreshToken,
        boolean isRegistered
) {
        public static AuthSignInResponseDto from (String accessToken, String refreshToken, boolean isRegistered) {
            return AuthSignInResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .isRegistered(isRegistered)
                    .build();
        }
}
