package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeReadyRequestDto(
        String partnerOrderId,
        String partnerUserId,
        Long cake,
        String taxFreeAmount,
        String vatAmount,
        String approvalUrl,
        String cancelUrl,
        String failUrl
) {

}
