package org.arsonal.wechatShop.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationCodeCheckService {
    private Map<String, String> verificationCodeMap = new ConcurrentHashMap<>();

    public void addCode(String tel, String correctCode) {
        verificationCodeMap.put(tel, correctCode);
    }

    public String getCorrectCode(String tel) {
        return verificationCodeMap.get(tel);
    }
}
