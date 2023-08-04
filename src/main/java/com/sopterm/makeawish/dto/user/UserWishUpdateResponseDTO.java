package com.sopterm.makeawish.dto.user;

import static java.util.Objects.*;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import lombok.Builder;

@Builder
public record UserWishUpdateResponseDTO(
	String startDate,
	String endDate,
	String phone,
	AccountInfo accountInfo
) {
	public static UserWishUpdateResponseDTO of(User user, Wish wish) {
		return UserWishUpdateResponseDTO.builder()
			.startDate(wish.getStartAt().toString())
			.endDate(wish.getEndAt().toString())
			.phone(user.getPhoneNumber())
			.accountInfo(nonNull(user.getAccount()) ? user.getAccount() : null)
			.build();
	}
}
