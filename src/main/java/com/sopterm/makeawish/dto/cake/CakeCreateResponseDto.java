package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeCreateResponseDto(
        Long cake,
        String imageUrl,
        String hint1,
        String hint2,
        String contribute,
        String wisher
) {
}
