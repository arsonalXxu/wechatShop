package org.arsonal.wechatShop.service;

public interface SmsCodeService {
    /**
     * 向目标手机号发送手机验证码.
     * 考虑风险：
     * 1、做流量控制，不能无限制发，防止被被人利用做短信轰炸；
     * 2、防止暴力破解，要限制次数，6位验证码尝试100万次可以破解；
     * @param tel 目标手机号
     * @return 手机验证码
     */
    String sendSmsCode(String tel);
}
