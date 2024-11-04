package com.sopterm.makeawish.service;

import com.popbill.api.KakaoService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.common.Util;
import com.sopterm.makeawish.domain.AlarmTemplate;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.alarm.AlarmRequestDTO;
import com.sopterm.makeawish.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AlarmService {
    @Value("${popbill.businessNumber}")
    private String corpNum;

    @Value("${popbill.isUseKkoTalk}")
    private String isUseKkoTalk;

    @Value("${popbill.senderNum}")
    private String senderNum;

    @Value("${popbill.linkId}")
    private String linkId;

    private final KakaoService kakaoService;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void sendAlarmTalk(User user, String templateName) {
        if(isUseKkoTalk.equals("false")) {
            return;
        }
        AlarmRequestDTO template = AlarmRequestDTO.of(findByTemplateName(templateName));
        try {
            String response = kakaoService.sendATS(corpNum, template.code(), senderNum, template.content(), template.altSubject(), template.altContent(), template.altSendType() ,user.getPhoneNumber(), user.getNickname(), Util.getCurrentTime(), linkId, user.getId()+"_"+ templateName + Util.getCurrentTime(), template.kakaoButtons());
        } catch(PopbillException e){
            e.printStackTrace(); //todo 예외 던지기
        }
    }

    private AlarmTemplate findByTemplateName(String templateName){
        return alarmRepository.findByName(templateName);
    }



}
