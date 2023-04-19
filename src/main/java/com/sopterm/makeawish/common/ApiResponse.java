package com.sopterm.makeawish.common;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ApiResponse(int status, boolean success, String message, Object data) {

	public static ApiResponse success(String message, Object data) {
		return ApiResponse.builder()
			.success(true)
			.message(message)
			.data(data)
			.build();
	}

	public static ApiResponse success(String message) {
		return ApiResponse.builder()
			.success(true)
			.message(message)
			.build();
	}

	public static ApiResponse fail(String message) {
		return ApiResponse.builder()
			.success(false)
			.message(message)
			.build();
	}

	public static ApiResponse of(int status, boolean success, String message, Object data) {
		return ApiResponse.builder()
				.status(status)
				.success(success)
				.message(message)
				.data(data)
				.build();
	}

	public static ApiResponse of(int status, boolean success, String message) {
		return ApiResponse.builder()
				.status(status)
				.success(success)
				.message(message)
				.build();
	}
}
