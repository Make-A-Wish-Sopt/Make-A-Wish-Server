package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeRequest(
        String name,
        Long cakeId,
        String message,
        Long wishId
) {
}
