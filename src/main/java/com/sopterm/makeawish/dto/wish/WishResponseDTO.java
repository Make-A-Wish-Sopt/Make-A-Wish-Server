package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;
import static java.util.Objects.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.sopterm.makeawish.domain.wish.Wish;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Builder
public record WishResponseDTO(String name, long dayCount, String title, String hint, String bank, String accountNumber) {

	public static WishResponseDTO from(Wish wish) {
		val name = nonNull(wish.getWisher().getAccount())
			? wish.getWisher().getAccount().getName()
			: wish.getWisher().getNickname();

		val account = nonNull(wish.getWisher().getAccount())
				? wish.getWisher().getAccount().getAccount()
				: StringUtils.EMPTY;
		val bank = nonNull(wish.getWisher().getAccount())
				? wish.getWisher().getAccount().getBank()
				: StringUtils.EMPTY;

		return WishResponseDTO.builder()
			.name(name)
			.accountNumber(account)
			.bank(bank)
			.dayCount(getRemainDayCount(wish.getEndAt()))
			.title(wish.getTitle())
			.hint(wish.getHint())
			.build();
	}

	private static long getRemainDayCount(LocalDateTime endAt) {
		val now = LocalDateTime.now();
		if (now.isAfter(endAt)) {
			throw new IllegalArgumentException(EXPIRE_WISH.getMessage());
		}
		return ChronoUnit.DAYS.between(now, endAt);
	}
}
