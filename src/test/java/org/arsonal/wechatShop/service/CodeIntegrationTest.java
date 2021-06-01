package org.arsonal.wechatShop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import org.arsonal.wechatShop.WechatShopApplication;
import org.arsonal.wechatShop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WechatShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class CodeIntegrationTest {
    @Autowired
    Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String getUrl(String apiName) {
        // 获取集成测试的端口
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

    private static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> heads;

        HttpResponse(int code, String body, Map<String, List<String>> heads) {
            this.code = code;
            this.body = body;
            this.heads = heads;
        }
    }

    private HttpResponse doHttpRequest(String apiName, boolean isGet, Object httpRequestBody,
                                       String cookie) throws JsonProcessingException {
        HttpRequest httpRequest = isGet ? HttpRequest.get(getUrl(apiName)) : HttpRequest.post(getUrl(apiName));

        httpRequest.contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
        if (cookie != null) {
            httpRequest.header("Cookie", cookie);
        }

        if (httpRequestBody != null) {
            httpRequest.send(objectMapper.writeValueAsString(httpRequestBody));
        }

        return new HttpResponse(httpRequest.code(), httpRequest.body(), httpRequest.headers());
    }

    @Test
    public void loginLogoutTest() throws Exception {

        // 最开始默认情况下，访问/api/status处于未登录状态
        String responseBody = doHttpRequest("/api/status", true, null, null).body;
//                HttpRequest.get(getUrl("/api/status"))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .body();
        LoginResponse response = objectMapper.readValue(responseBody, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());

        // 发送验证码
        int responseCode = doHttpRequest("/api/code", false, TelVerificationServiceTest.VALID_PARAMETER,
                null).code;
//                HttpRequest.post(getUrl("/api/code"))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.VALID_PARAMETER))
//                .code();

        Assertions.assertEquals(HTTP_OK, responseCode);

        // 带着验证码进行登录，得到cookie
        Map<String, List<String>> responseHeads = doHttpRequest("/api/login", false,
                TelVerificationServiceTest.VALID_PARAMETER_CODE, null).heads;

        final List<String> setCookies = responseHeads.get("Set-Cookie");
        Assertions.assertNotNull(setCookies);
        final String sessionId = getSessinIdfromSetCookie(
                setCookies.stream().filter(cookie -> cookie.contains("JSESSIONID")).findFirst().get());

        // 带着cookie访问，/api/status应该处于登录状态
        responseBody = doHttpRequest("/api/status", true, null, sessionId).body;

        response = objectMapper.readValue(responseBody, LoginResponse.class);
        Assertions.assertTrue(response.isLogin());
        Assertions.assertEquals(TelVerificationServiceTest.VALID_PARAMETER.getTel(), response.getUser().getTel());

        // 调用/api/logout
        // 注销登录，也需要带着Cookie因为不带的话服务器根本就不知道是谁注销登录
        doHttpRequest("/api/logout", false, null, sessionId);

        // 再次带着cookie访问/api/status恢复成未登录状态
        responseBody = doHttpRequest("/api/status", true, null, sessionId).body;

        response = objectMapper.readValue(responseBody, LoginResponse.class);
        Assertions.assertFalse(response.isLogin());
    }

    private String getSessinIdfromSetCookie(String setCookie) {
        final int semiColonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semiColonIndex);
    }

    @Test
    public void returnHttpOkWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.VALID_PARAMETER))
                .code();
        Assertions.assertEquals(HTTP_OK, responseCode);
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsWrong() throws JsonProcessingException {
        int responseCode = HttpRequest.post(getUrl("/api/code"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(TelVerificationServiceTest.EMPTY_TEL))
                .code();
        Assertions.assertEquals(HTTP_BAD_REQUEST, responseCode);
    }
}
