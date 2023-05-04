package com.sopterm.makeawish.dto.cake;

import com.sopterm.makeawish.domain.Cake;
import lombok.Builder;

@Builder
public record CakeApproveRequestDto(
        String pg_token,
        String tid,
        String partner_order_id,
        String partner_user_id,
        String name,
        String cake,
        String price,
        String message,
        Long wishId
) {
}
