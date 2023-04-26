package com.sopterm.makeawish.common.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
	EXPIRE_WISH("주간이 끝난 소원 링크입니다."),
	INVALID_WISH("존재하지 않는 소원 링크입니다."),
	INVALID_USER("존재하지 않는 회원입니다."),
	NULL_PRINCIPAL("principal 이 null 일 수 없습니다.");

	private final String message;
}
