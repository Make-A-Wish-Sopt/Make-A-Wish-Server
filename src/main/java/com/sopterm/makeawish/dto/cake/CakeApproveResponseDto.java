package com.sopterm.makeawish.dto.cake;

public record CakeApproveResponseDto(
    String aid,
    String tid,
    String cid,
    String sid,
    String partner_order_id,
    String partner_user_id,
    String payment_method_type,
    String item_name,
    String item_code,
    int quantity,
    Amount amount,
    CardInfo cardInfo,
    String created_at,
    String approved_at,
    String payload
) {
}
