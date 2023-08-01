package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.Util.*;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record UserWishResponseDTO(
	String title,
	String startAt,
	String endAt,
	int price,
	int percent
) {

	public static UserWishResponseDTO of(Wish wish) {
		return UserWishResponseDTO.builder()
			.title(wish.getTitle())
			.startAt(wish.getStartAt().toString())
			.endAt(wish.getEndAt().toString())
			.price(getPriceAppliedFee(wish.getTotalPrice()))
			.percent(getPricePercent(wish.getTotalPrice(), wish.getPresentPrice()))
			.build();
	}
}
