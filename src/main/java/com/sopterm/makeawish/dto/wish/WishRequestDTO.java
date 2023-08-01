package com.sopterm.makeawish.dto.wish;

import java.time.LocalDateTime;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public record WishRequestDTO(
	String imageUrl,
	int price,
	String title,
	String hint,
	String initial,
	String startDate,
	String endDate,
	String phone
) {

	public Wish toEntity(User wisher) {
		return Wish.builder()
			.presentImageUrl(imageUrl)
			.presentPrice(price)
			.title(title)
			.hint(hint)
			.initial(initial)
			.startAt(convertToTime(startDate))
			.endAt(convertToTime(endDate))
			.phoneNumber(phone)
			.wisher(wisher)
			.build();
	}

	public static LocalDateTime convertToTime(String date) {
		return LocalDateTime.parse(date);
	}
}
