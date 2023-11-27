package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeCreateRequest(
        String name,
        Long cakeId,
        String message,
        Long wishId
) {
}
