package com.sopterm.makeawish.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import lombok.val;

public class Util {

	private static final float FEE = 3.4f;

	public static int getPriceAppliedFee(int price) {
		return (int)Math.floor(price * (1 - FEE));
	}

	public static int getPricePercent(int totalPrice, int presentPrice) {
		return (getPriceAppliedFee(totalPrice) / presentPrice) * 100;
	}

	public static LocalDateTime convertToTime(String date) {
		if(Objects.isNull(date)) return null;
		val instant = Instant
			.from(DateTimeFormatter.ISO_DATE_TIME.parse(date))
			.atZone(ZoneId.of("Asia/Seoul"));
		return LocalDateTime.from(instant);
	}
}
