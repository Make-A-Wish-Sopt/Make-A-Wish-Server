package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

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
		val realTotalPrice = getTotalPriceAppliedFee(wish.getTotalPrice());

		return MainWishResponseDTO.builder()
			.wishId(wish.getId())
			.name(name)
			.cakeCount(wish.getPresents().size())
			.dayCount(getRemainDay(wish.getEndAt()))
			.price(realTotalPrice)
			.percent(getPercent(wish.getPresentPrice(), realTotalPrice))
			.build();
	}

	private static int getTotalPriceAppliedFee(int price) {
		return (int)Math.floor(price * (1 - 3.4));
	}

	private static int getPercent(int presentPrice, int totalPrice) {
		return (totalPrice / presentPrice) * 100;
	}

	private static long getRemainDay(LocalDateTime endAt) {
		val now = LocalDateTime.now();
		if (now.isAfter(endAt)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
