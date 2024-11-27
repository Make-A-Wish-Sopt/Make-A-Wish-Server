package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.TransferInfo;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;
import lombok.val;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.sopterm.makeawish.common.message.ErrorMessage.EXPIRE_WISH;

@Builder
public record WishResponseDTO(long dayCount, String title, String hint, boolean wantsGift, String presentImageUrl, TransferInfo transferInfo) {

	public static WishResponseDTO from(Wish wish) {
//		UserTransferInfo userTransferInfo = wish.getWisher().getUserTransferInfo();
//		val name = nonNull(userTransferInfo.getAccountInfo().getName())
//			? userTransferInfo.getAccountInfo().getName()
//			: wish.getWisher().getNickname();
//
//		val account = nonNull(wish.getWisher().getUserTransferInfo().getAccountInfo().getAccount())
//				? userTransferInfo.getAccountInfo().getAccount()
//				: StringUtils.EMPTY;
//		val bank = nonNull(wish.getWisher().getUserTransferInfo().)
//				? userTransferInfo.getAccount().getBank()
//				: StringUtils.EMPTY;
//
//		val kakaoPayCode = nonNull(wish.getWisher().getAccount().getKakaoPayCode())
//				? userTransferInfo.getKakaoPayCode()
//				: StringUtils.EMPTY;

		return WishResponseDTO.builder()
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
