package com.sopterm.makeawish.dto.wish;

import static com.sopterm.makeawish.common.Util.*;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public record WishRequestDTO(
	String imageUrl,
	String title,
	String startDate,
	String endDate,
	String phone,
	boolean wantsGift
) {

	public Wish toEntity(User wisher) {
		wisher.updatePhoneNumber(phone);
		return Wish.builder()
			.presentImageUrl(imageUrl)
			.title(title)
			.startAt(convertToDate(startDate))
			.endAt(convertToDate(endDate))
			.wisher(wisher)
			.wantsGift(wantsGift)
			.build();
	}
}
