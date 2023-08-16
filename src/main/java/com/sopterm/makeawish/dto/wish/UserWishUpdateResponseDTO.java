package com.sopterm.makeawish.dto.wish;

import static java.util.Objects.*;

import com.sopterm.makeawish.domain.user.AccountInfo;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;
import com.sopterm.makeawish.domain.wish.WishStatus;

import lombok.Builder;

@Builder
public record UserWishUpdateResponseDTO(
	String startDate,
	String endDate,
	String phone,
	AccountInfo accountInfo,
	String imageUrl,
	String title,
	int price,
	String initial,
	String hint,
	WishStatus status
) {
	public static UserWishUpdateResponseDTO of(User user, Wish wish) {
		return UserWishUpdateResponseDTO.builder()
			.startDate(wish.getStartAt().toString())
			.endDate(wish.getEndAt().toString())
			.phone(user.getPhoneNumber())
			.accountInfo(nonNull(user.getAccount()) ? user.getAccount() : null)
			.imageUrl(wish.getPresentImageUrl())
			.title(wish.getTitle())
			.price(wish.getPresentPrice())
			.initial(wish.getInitial())
			.hint(wish.getHint())
			.status(wish.getStatus(0))
			.build();
	}
}
