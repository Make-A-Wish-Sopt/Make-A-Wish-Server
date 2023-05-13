package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record MypageWishUpdateResponseDTO(String startDate, String endDate, String name, String bankName, String account, String phone) {

	public static MypageWishUpdateResponseDTO from(Wish wish) {
		return MypageWishUpdateResponseDTO.builder()
			.startDate(wish.getStartAt().toString())
			.endDate(wish.getEndAt().toString())
			.name(wish.getAccount().getName())
			.account(wish.getAccount().getAccount())
			.bankName(wish.getAccount().getBank())
			.phone(wish.getPhoneNumber())
			.build();
	}
}
