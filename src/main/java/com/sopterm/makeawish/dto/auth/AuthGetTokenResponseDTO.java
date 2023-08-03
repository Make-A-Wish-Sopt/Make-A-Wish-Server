package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthGetTokenResponseDTO(
        @NonNull
        String accessToken,
        @NonNull
        String refreshToken
) {
}
