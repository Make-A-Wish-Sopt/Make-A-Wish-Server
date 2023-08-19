package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthSignInResponseDTO(
        @NonNull
        String accessToken,
        @NonNull
        String refreshToken,
        @NonNull
        String nickName
) {
        public static AuthSignInResponseDTO from (String accessToken, String refreshToken, String nickName) {
            return AuthSignInResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .nickName(nickName)
                    .build();
        }
}
