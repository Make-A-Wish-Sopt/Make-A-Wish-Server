package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.Util.*;
import static com.sopterm.makeawish.domain.wish.WishStatus.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.domain.wish.WishStatus;

import lombok.*;

@Builder
public record MainWishResponseDTO(
	Long wishId,
	String name,
	int cakeCount,
	long dayCount,
	int price,
	int percent,
	WishStatus status
) {

	public static MainWishResponseDTO from(Wish wish) {
		val name = Objects.nonNull(wish.getWisher().getAccount())
			? wish.getWisher().getAccount().getName()
			: wish.getWisher().getNickname();

		return MainWishResponseDTO.builder()
			.wishId(wish.getId())
			.name(name)
			.cakeCount(wish.getPresents().size())
			.dayCount(getRemainDay(wish))
			.price(getPriceAppliedFee(wish.getTotalPrice()))
			.percent(getPricePercent(wish.getTotalPrice(), wish.getPresentPrice()))
			.status(wish.getStatus(0))
			.build();
	}

	private static long getRemainDay(Wish wish) {
		val now = LocalDateTime.now();
		return wish.getStatus(0).equals(BEFORE)
			? ChronoUnit.DAYS.between(now, wish.getStartAt())
			: ChronoUnit.DAYS.between(now, wish.getEndAt());
	}
}