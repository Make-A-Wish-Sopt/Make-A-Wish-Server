package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record WishResponseDTO(String userName, long dayCount, String title, String hint) {

	public static WishResponseDTO from(Wish wish) {
		return WishResponseDTO.builder()
			.userName(wish.getWisher().getNickname())
			.dayCount(getRemainDayCount(wish.getEndAt()))
			.title(wish.getTitle())
			.hint(wish.getHint())
			.build();
	}

	private static long getRemainDayCount(LocalDateTime endAt) {
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(endAt)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
