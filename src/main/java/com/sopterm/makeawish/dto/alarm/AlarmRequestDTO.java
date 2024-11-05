package com.sopterm.makeawish.dto.alarm;

import com.popbill.api.kakao.KakaoButton;
import com.sopterm.makeawish.domain.AlarmTemplate;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public record AlarmRequestDTO(
        String code
        , String senderNum
        , String content
        , String altSubject
        , String altContent
        , String altSendType
        , KakaoButton[] kakaoButtons
) {
    public static AlarmRequestDTO of(AlarmTemplate alarmTemplate, String...params){
        KakaoButton[] buttons = initButtons(alarmTemplate.getButton(), "WL" ,alarmTemplate.getButtonUrl());
        return AlarmRequestDTO.builder()
                .code(alarmTemplate.getCode())
                .content(alarmTemplate.replaceContent(params))
                .altSubject(StringUtils.EMPTY)
                .altContent(StringUtils.EMPTY)
                .altSendType("C")
                .kakaoButtons(buttons)
                .build();

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
