package com.sopterm.makeawish.dto.wish;

import lombok.Builder;

@Builder
public record UserWishUpdateRequestDTO(
	String startDate,
	String endDate,
	String name,
	String bankName,
	String account,
	String imageUrl,
	String title,
	boolean wantsGift
) {
}
