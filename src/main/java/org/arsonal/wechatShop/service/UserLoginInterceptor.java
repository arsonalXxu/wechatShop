package org.arsonal.wechatShop.service;

import org.apache.shiro.SecurityUtils;
import org.arsonal.wechatShop.generate.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Object tel = SecurityUtils.getSubject().getPrincipal();
        if (tel != null) {
            // 说明已经登录了
            User user = userService.getUserByTel(tel.toString());
            UserContext.setCurrentUser(user);
        }
        System.out.println("Pre!");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 非常重要，因为线程是会被复用的，如果在线程1中保存了用户A的信息，并且没有被清理，
        // 那么下次线程1再用来处理别的请求的时候，就会出现“串号”的情况
        UserContext.setCurrentUser(null);
    }
}
