package com.sopterm.makeawish.service;

import com.popbill.api.KakaoService;
import com.popbill.api.PopbillException;
import com.sopterm.makeawish.common.Util;
import com.sopterm.makeawish.domain.AlarmTemplate;
import com.sopterm.makeawish.domain.user.User;
import com.sopterm.makeawish.dto.alarm.AlarmRequestDTO;
import com.sopterm.makeawish.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AlarmService {
    @Value("corpNum")
    private String corpNum;

    @Value("isUseAlarmTalk")
    private boolean isUserAlarmTalk;

    @Value("senderNum")
    private String senderNum;
    private final String requestNum = StringUtils.EMPTY;

    private final KakaoService kakaoService;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void sendAlarmTalk(User user, String templateName) {
        if(!isUserAlarmTalk) {
            return;
        }
        AlarmRequestDTO template = AlarmRequestDTO.of(findByTemplateName(templateName));
        try {
            String response = kakaoService.sendATS(corpNum, template.code(), senderNum, template.content(), "","" ,user.getPhoneNumber(), user.getNickname(), Util.getCurrentTime(), String.valueOf(user.getId()), requestNum, template.kakaoButtons());
        } catch(PopbillException e){
            e.printStackTrace();
        }
    }

    private AlarmTemplate findByTemplateName(String templateName){
        return alarmRepository.findByTemplateName(templateName);
    }



}
