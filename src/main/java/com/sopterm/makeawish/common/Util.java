package com.sopterm.makeawish.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.val;

public class Util {

	private static final float FEE = 0f;
	public static int getPriceAppliedFee(int price) {
		return (int)Math.floor(price * (1 - FEE));
	}

	public static int getPricePercent(int totalPrice, int presentPrice) {
		float percent = ((float)getPriceAppliedFee(totalPrice) / (float)presentPrice) * 100;
		return Math.round(percent);
	}

	public static LocalDateTime convertToDate(String date) {
		return convertToTime(date).toLocalDate().atStartOfDay();
	}

	private static LocalDateTime convertToTime(String dateTime) throws DateTimeParseException {
		val instant = Instant
			.from(DateTimeFormatter.ISO_DATE_TIME.parse(dateTime))
			.atZone(ZoneId.of("Asia/Seoul"));
		return LocalDateTime.from(instant);
	}

	public static String calculateContribution(int price, int targetPrice) {
		return String.format("%.0f", (double) price / targetPrice * 100);
	}
}
