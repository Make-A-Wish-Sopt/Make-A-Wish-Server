package com.sopterm.makeawish.dto.cake;

import lombok.Builder;

@Builder
public record CakeReadyRequestDto(
        String partner_order_id,
        String partner_user_id,
        String item_name,
        String total_amount,
        String tax_free_amount,
        String approval_url,
        String cancel_url,
        String fail_url
) {

}
