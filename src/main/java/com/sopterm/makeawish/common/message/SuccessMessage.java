package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessage {

	/** auth **/
	SUCCESS_SIGN_IN("소셜 로그인 성공"),
	SUCCESS_GET_REFRESH_TOKEN("토큰 재발급 성공"),

	/** wish **/
	SUCCESS_CREATE_WISH("소원 링크 생성 성공"),

	/** cake **/
	SUCCESS_GET_ALL_CAKE("케이크 전체 조회 성공"),
	SUCCESS_GET_READY_KAKAOPAY("카카오페이 결제 준비 성공");

	private final String message;
}
