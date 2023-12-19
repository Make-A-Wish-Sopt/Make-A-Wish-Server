package com.sopterm.makeawish.common.message.slack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackErrorMessage {
    POST_REQUEST_ERROR("슬랙 요청이 실패했습니다 :");

    private final String message;
}
