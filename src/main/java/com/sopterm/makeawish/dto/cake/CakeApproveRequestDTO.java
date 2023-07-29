package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeApproveRequestDTO(
        String pgToken,
        String tid,
        String partnerOrderId,
        String partnerUserId,
        String name,
        Long cakeId,
        String message,
        Long wishId
) {
}
