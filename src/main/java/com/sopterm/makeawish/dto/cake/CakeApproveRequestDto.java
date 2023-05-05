package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeApproveRequestDto(
        String pg_token,
        String tid,
        String partner_order_id,
        String partner_user_id,
        String name,
        Long cake,
        String message,
        Long wishId
) {
}
