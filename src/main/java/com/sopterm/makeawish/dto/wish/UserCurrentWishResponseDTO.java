package com.sopterm.makeawish.dto.wish;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

import java.util.Objects;

@Builder
public record UserCurrentWishResponseDTO(String startDate, String endDate, String phone, AccountInfo accountInfo) {

	private static AccountInfo createAccount(User user) {
		if(Objects.isNull(user.getAccount())) {
			return null;
		}
		return new AccountInfo(user.getAccount().getName(),user.getAccount().getBank(),user.getAccount().getAccount());
	}

	public static UserCurrentWishResponseDTO from(Wish wish, User user) {
		return UserCurrentWishResponseDTO.builder()
			.startDate(wish.getStartAt().toString())
			.endDate(wish.getEndAt().toString())
			.accountInfo(createAccount(user))
			.phone(wish.getPhoneNumber())
			.build();
	}
}
