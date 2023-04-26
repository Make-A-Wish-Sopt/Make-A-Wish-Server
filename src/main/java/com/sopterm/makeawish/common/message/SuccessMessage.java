package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessage {

	// cake
	SUCCESS_GET_ALL_CAKE("케이크 전체 조회 성공");

	private final String message;
}
