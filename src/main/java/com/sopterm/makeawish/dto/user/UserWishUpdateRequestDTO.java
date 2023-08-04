package com.sopterm.makeawish.dto.user;

import lombok.Builder;

@Builder
public record UserWishUpdateRequestDTO(
    String startDate,
    String endDate,
    String name,
    String bankName,
    String account,
    String phone
) {
}
