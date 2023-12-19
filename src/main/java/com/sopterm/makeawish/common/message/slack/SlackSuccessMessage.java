package com.sopterm.makeawish.common.message.slack;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlackSuccessMessage {
    SUCCESS_CREATE_WISH("새로운 소원이 생성되었어요!")
    , SUCCESS_CREAT_USER("새로운 유저가 가입했어요!");

    private final String message;
}
