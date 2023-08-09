package com.sopterm.makeawish.dto.wish;

import lombok.Builder;

@Builder
public record UserWishUpdateRequestDTO(
    String startDate,
    String endDate,
    String name,
    String bankName,
    String account,
    String phone,
	String imageUrl,
	Integer price,
	String title,
	String hint,
	String initial
) {
}
