package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeApproveRequestDto(
        String pgToken,
        String tid,
        String partnerOrderId,
        String partnerUserId,
        String name,
        Long cake,
        String message,
        Long wishId
) {
}
