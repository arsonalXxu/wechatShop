package org.arsonal.wechatShop.service;

import org.arsonal.wechatShop.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TelVerificationService {
    private static Pattern TEL_PATTERN = Pattern.compile("1\\d{10}");
    /**
     * 验证输入的参数是否合法：
     * tel必须存在，且为合法的中国大陆手机号
     * @param telAndCode 输入的参数
     * @return true 合法，否则返回false。
     */
    public boolean verifyTelParameters(AuthController.TelAndCode telAndCode) {
        if (telAndCode == null) {
            return false;
        } else if (telAndCode.getTel() == null) {
            return false;
        } else {
            return TEL_PATTERN.matcher(telAndCode.getTel()).find();
        }
    }
}
