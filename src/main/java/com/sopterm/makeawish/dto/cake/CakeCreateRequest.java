package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeCreateRequest(
        String name,
        String message,
        Long cakeId,
        Long wishId
) {
}
