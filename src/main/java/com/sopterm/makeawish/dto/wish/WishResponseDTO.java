package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.TransferInfo;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;
import lombok.val;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sopterm.makeawish.common.message.ErrorMessage.EXPIRE_WISH;

@Builder
public record WishResponseDTO(long dayCount, String title, String hint, boolean wantsGift, String presentImageUrl, TransferInfo transferInfo, String nickname) {

	public static WishResponseDTO from(Wish wish) {
		return WishResponseDTO.builder()
			.nickname(wish.getWisher().getNickname())
			.transferInfo(wish.getWisher().getTransferInfo())
			.dayCount(getRemainDayCount(wish.getEndAt()))
			.title(wish.getTitle())
			.hint(wish.getHint())
			.wantsGift(wish.isWantsGift())
			.presentImageUrl(wish.getPresentImageUrl())
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
