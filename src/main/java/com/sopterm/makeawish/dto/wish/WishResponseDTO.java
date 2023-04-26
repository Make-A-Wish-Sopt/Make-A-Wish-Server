package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.TimeZone;

import com.sopterm.makeawish.common.message.ErrorMessage;
import com.sopterm.makeawish.domain.wish.Wish;

import lombok.Builder;

@Builder
public record WishResponseDTO(String name, int dayCount, String title, String hint) {

	public static WishResponseDTO from(Wish wish) {
		return WishResponseDTO.builder()
			.name(wish.getWisher().getAccount().getName())
			.dayCount(getRemainDay(wish.getEndAt()))
			.title(wish.getTitle())
			.hint(wish.getHint1())
			.build();
	}

	private static int getRemainDay(LocalDateTime date) {
		LocalDate now = LocalDate.now((ZoneId.of("Asia/Seoul")));
		LocalDate endDate = LocalDate.from(date);
		if (now.isAfter(endDate)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return Period.between(now, endDate).getDays();
	}
}
