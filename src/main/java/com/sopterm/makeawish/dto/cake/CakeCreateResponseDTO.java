package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeCreateResponseDTO(
        Long cakeId,
        String imageUrl,
        String hint,
        String initial,
        String contribute,
        String wisher
) {
}
