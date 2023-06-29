package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthSignInResponseDto(
        @NonNull
        String accessToken
) {
        public static AuthSignInResponseDto from (String accessToken) {
            return AuthSignInResponseDto.builder()
                    .accessToken(accessToken)
                    .build();
        }
}
