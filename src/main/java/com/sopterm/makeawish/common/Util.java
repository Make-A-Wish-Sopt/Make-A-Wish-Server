package com.sopterm.makeawish.common;

public class Util {

	private static final float FEE = 3.4f;

	public static int getPriceAppliedFee(int price) {
		return (int)Math.floor(price * (1 - FEE));
	}

	public static int getPricePercent(int totalPrice, int presentPrice) {
		return (getPriceAppliedFee(totalPrice) / presentPrice) * 100;
	}
}
