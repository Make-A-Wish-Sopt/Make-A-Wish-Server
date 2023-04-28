package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeReadyResponseDto(
        String tid,
        String next_redirect_app_url,
        String next_redirect_mobile_url,
        String next_redirect_pc_url,
        String created_at
) {
}
