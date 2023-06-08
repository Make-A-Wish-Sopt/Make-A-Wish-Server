package com.sopterm.makeawish.dto.wish;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.domain.wish.Wish;

public record WishRequestDTO(String imageUrl, int price, String title, String hint1, String hint2, String startDate,
							 String endDate, String name, String bankName, String account, String phone) {

	public Wish toEntity(User wisher) {
		return Wish.builder()
			.presentImageUrl(imageUrl)
			.presentPrice(price)
			.title(title)
			.hint1(hint1)
			.hint2(hint2)
			.startAt(convertToTime(startDate))
			.endAt(convertToTime(endDate))
			.phoneNumber(phone)
			.wisher(wisher)
			.build();
	}

	private LocalDateTime convertToTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		return LocalDateTime.parse(date + " 00:00", formatter);
	}
}
