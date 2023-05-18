package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessage {

	/** auth **/
	SUCCESS_SIGN_IN("소셜 로그인 성공"),
	SUCCESS_GET_REFRESH_TOKEN("토큰 재발급 성공"),

	/** user **/
	SUCCESS_UPDATE_USER_INFO("유저 소원 정보 수정 성공"),
	SUCCESS_GET_USER_INFO("유저 소원 정보 가져오기 성공"),

	/** wish **/
	SUCCESS_CREATE_WISH("소원 링크 생성 성공"),
	SUCCESS_FIND_WISH("소원 링크 조회 성공"),
	NO_WISH("진행 중인 소원이 없습니다."),
	SUCCESS_GET_MAIN_WISH("진행 중인 소원 조회 성공"),
	SUCCESS_PARSE_HTML("HTML 파싱 성공"),

	/** cake **/
	SUCCESS_GET_ALL_CAKE("케이크 전체 조회 성공"),
	SUCCESS_GET_READY_KAKAOPAY("카카오페이 결제 준비 성공"),
	SUCCESS_CREATE_CAKE("케이크 저장 성공"),
	SUCCESS_GET_PGTOKEN("pg 토큰 전달 성공"),
	SUCCESS_GET_ALL_PRESENT("선물 전체 조회 성공");
	private final String message;
}
