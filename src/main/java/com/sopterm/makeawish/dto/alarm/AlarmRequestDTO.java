package com.sopterm.makeawish.dto.alarm;

import com.popbill.api.kakao.KakaoButton;
import com.sopterm.makeawish.common.Util;
import com.sopterm.makeawish.domain.AlarmTemplate;
import lombok.Builder;

@Builder
public record AlarmRequestDTO(
        String templateCode
        , String senderNum
        , String content
        , String altContent
        , String altSendType
        , KakaoButton[] kakaoButtons
) {
    public static AlarmRequestDTO of(AlarmTemplate alarmTemplate){
        KakaoButton[] buttons = initButtons(alarmTemplate.getButton(), "WL" ,alarmTemplate.getButtonUrl());
        return AlarmRequestDTO.builder()
                .templateCode(alarmTemplate.getCode())
                .content(alarmTemplate.getContent())
                .altContent()
                .altSendType()
                ;

    }

    private static KakaoButton[] initButtons(String buttonName, String buttonType, String buttonLink){
        KakaoButton[] kakaoButtons = new KakaoButton[1];
        KakaoButton button = new KakaoButton();
        button.setN(buttonName);
        button.setT(buttonType);
        button.setU1(buttonLink);
        button.setU2(buttonLink);
        kakaoButtons[0] = button;

        return kakaoButtons;
    }
}
