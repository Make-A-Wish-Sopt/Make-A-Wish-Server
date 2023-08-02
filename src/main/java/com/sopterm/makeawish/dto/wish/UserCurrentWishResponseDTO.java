package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record UserCurrentWishResponseDTO(String startDate, String endDate, String phone, AccountInfo accountInfo) {

	public static MypageWishResponseDTO from(Wish wish, User user) {
		return MypageWishResponseDTO.builder()
			.startDate(wish.getStartAt().toString())
			.endDate(wish.getEndAt().toString())
			.accountInfo(new AccountInfo(user.getAccount().getName(),user.getAccount().getBank(),user.getAccount().getAccount()))
			.phone(wish.getPhoneNumber())
			.build();
	}
}
