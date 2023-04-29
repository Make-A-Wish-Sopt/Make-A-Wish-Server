package com.sopterm.makeawish.dto.cake;

public record CakeApproveResponseDto(
        boolean isPaid,
        String imageUrl,
        int cakeType,
        String contribute,
        String wisher
) {
}
