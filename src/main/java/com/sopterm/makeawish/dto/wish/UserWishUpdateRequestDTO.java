package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.TransferInfo;
import lombok.Builder;

@Builder
public record UserWishUpdateRequestDTO(
	String startDate,
	String endDate,
	String imageUrl,
	String title,
	boolean wantsGift,
	TransferInfo transferInfo
) {
}
