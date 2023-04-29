package com.sopterm.makeawish.dto.cake;

public record CakeApproveRequestDto(
        boolean isPaid,
        String pg_token,
        String tid,
        String partner_order_id,
        String partner_user_id,
        String name,
        int cake,
        String letter
) {
}
