package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.Util.*;
import static com.sopterm.makeawish.domain.wish.WishStatus.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.domain.wish.WishStatus;

import lombok.*;

@Builder
public record MainWishResponseDTO(
	Long wishId,
	int cakeCount,
	long dayCount,
	WishStatus status
) {

	public static MainWishResponseDTO from(Wish wish) {
		return MainWishResponseDTO.builder()
			.wishId(wish.getId())
			.cakeCount(wish.getPresents().size())
			.dayCount(getRemainDay(wish))
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