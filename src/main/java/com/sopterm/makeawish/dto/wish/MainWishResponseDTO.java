package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.Util.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.*;

@Builder
public record MainWishResponseDTO(
	Long wishId,
	String name,
	int cakeCount,
	long dayCount,
	int price,
	int percent
) {

	public static MainWishResponseDTO from(Wish wish) {
		val name = Objects.nonNull(wish.getWisher().getAccount())
			? wish.getWisher().getAccount().getName()
			: wish.getWisher().getNickname();

		return MainWishResponseDTO.builder()
			.wishId(wish.getId())
			.name(name)
			.cakeCount(wish.getPresents().size())
			.dayCount(getRemainDay(wish.getEndAt()))
			.price(getPriceAppliedFee(wish.getTotalPrice()))
			.percent(getPricePercent(wish.getTotalPrice(), wish.getPresentPrice()))
			.build();
	}

	private static long getRemainDay(LocalDateTime endAt) {
		val now = LocalDateTime.now();
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
