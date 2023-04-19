package com.sopterm.makeawish.dto.auth;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record AuthGetTokenResponseDto(
        @NonNull
        String accessToken,
        @NonNull
        String refreshToken
) {
}
