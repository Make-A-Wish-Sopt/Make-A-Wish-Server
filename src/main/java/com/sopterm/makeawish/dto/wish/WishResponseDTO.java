package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record WishResponseDTO(String name, long dayCount, String title, String hint) {

	public static WishResponseDTO from(Wish wish) {
		String name = nonNull(wish.getWisher().getAccount())
			? wish.getWisher().getAccount().getName()
			: wish.getWisher().getNickname();

		return WishResponseDTO.builder()
			.name(name)
			.dayCount(getRemainDay(wish.getEndAt()))
			.title(wish.getTitle())
			.hint(wish.getHint1())
			.build();
	}

	private static long getRemainDay(LocalDateTime endAt) {
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(endAt)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
