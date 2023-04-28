package com.sopterm.makeawish.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoPayProperties {
    public static String adminKey;
    public static String cid;

    @Value("${kakaopay.admin-key}")
    public void setAdminKey(String adminKey){
        KakaoPayProperties.adminKey=adminKey;
    }

    @Value("${kakaopay.cid}")
    public void setCid(String cid){
        KakaoPayProperties.cid=cid;
    }
}
