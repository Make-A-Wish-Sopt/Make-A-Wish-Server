package com.sopterm.makeawish.domain;


import com.sopterm.makeawish.common.ResponseCode;
import com.sopterm.makeawish.exception.CustomException;

import java.util.Arrays;

public enum SocialType {
    KAKAO, GOOGLE;

    public static SocialType from(String social) {
        return Arrays.stream(SocialType.values())
                .filter(socialType -> socialType.toString().equals(social))
                .findFirst()
                .orElseThrow(() -> new CustomException(ResponseCode.INVALID_SOCIAL_TYPE));
    }
}
