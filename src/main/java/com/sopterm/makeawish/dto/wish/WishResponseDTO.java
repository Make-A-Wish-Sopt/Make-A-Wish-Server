package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record WishResponseDTO(String name, long dayCount, String title, String hint) {

	public static WishResponseDTO from(Wish wish) {
		return WishResponseDTO.builder()
			.name(wish.getWisher().getNickname())
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
