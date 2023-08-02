package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthSignInResponseDTO(
        @NonNull
        String accessToken,
        @NonNull
        String refreshToken
) {
        public static AuthSignInResponseDTO from (String accessToken, String refreshToken) {
            return AuthSignInResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
}
