package com.sopterm.makeawish.domain.user;

import java.util.Arrays;

import static com.sopterm.makeawish.common.message.ErrorMessage.INCORRECT_WISH;

public enum SocialType {
    KAKAO;

    public static SocialType from(String social) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.toString().equals(social))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INCORRECT_WISH.getMessage()));
    }
}
