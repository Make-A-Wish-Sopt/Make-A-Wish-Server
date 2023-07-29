package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	EXPIRE_WISH("주간이 끝난 소원 링크입니다."),
	INVALID_WISH("존재하지 않는 소원 링크입니다."),
	EXIST_MAIN_WISH("이미 진행 중인 소원 링크가 있습니다."),
	NO_EXIST_MAIN_WISH("수정할 수 있는 소원링크가 없습니다."),
	INVALID_USER("인증되지 않은 회원입니다."),
	NULL_PRINCIPAL("principal 이 null 일 수 없습니다."),
	INVALID_CAKE("존재하지 않는 케이크입니다."),
	INCORRECT_WISH("본인의 소원 링크가 아닙니다"),
	NOT_PAID_CAKE("결제가 필요한 케이크가 아닙니다"),
	DISAGREE_KAKAO_EMAIL("카카오 이메일 항목에 동의하지 않았습니다."),
	CODE_PARSE_ERROR("인가 코드 오류"),
	FAILED_VALIDATE_KAKAO_LOGIN("카카오 로그인 오류입니다."),
	WRONG_SIGNATURE("잘못된 서명이 사용되었습니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	INVALID_CODE("코드가 잘못되었습니다"),
	WRONG_TOKEN("토큰이 비어있거나 잘못된 토큰입니다."),
	SERVER_INTERNAL_ERROR("서버 내부 오류"),
	INVALID_HTTP_REQUEST("지원하지 않는 HTTP Method 요청입니다.");

	private final String message;
}
