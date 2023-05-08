package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record MainWishResponseDTO(Long wishId, String name, int cakeCount, long dayCount, int price,
								  int percent, String imageUrl) {

	public static MainWishResponseDTO from(Wish wish) {
		int calculatedPercent = getPercent(wish.getPresentPrice(), wish.getTotalPrice());
		return MainWishResponseDTO.builder()
			.wishId(wish.getId())
			.name(wish.getWisher().getAccount().getName())
			.cakeCount(wish.getPresents().size())
			.dayCount(getRemainDay(wish.getEndAt()))
			.price(wish.getTotalPrice())
			.percent(calculatedPercent)
			.imageUrl(getImageUrl(calculatedPercent))
			.build();
	}

	private static String getImageUrl(int percent) {
		if (percent < 0) {
			return "case1";
		} else if (percent == 0) {
			return "case2";
		} else {
			return "case3";
		}
	}

	private static int getPercent(int presentPrice, int totalPrice) {
		return (totalPrice / presentPrice) * 100;
	}

	private static long getRemainDay(LocalDateTime endAt) {
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(endAt)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
