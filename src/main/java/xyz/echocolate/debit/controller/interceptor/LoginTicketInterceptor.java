package xyz.echocolate.debit.controller.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.echocolate.debit.entity.LoginTicket;
import xyz.echocolate.debit.entity.User;
import xyz.echocolate.debit.service.UserService;
import xyz.echocolate.debit.util.CookieUtil;
import xyz.echocolate.debit.util.HostHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 登录凭证检查拦截器
 * @ClassName: LoginTicketInterceptor
 * @Author Cmite
 * @Date: 2022/6/30 12:54
 * @Version 1.0
 */

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 通过 cookie 得到凭证 ticket
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 判断凭证是否有效
            SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
            Date expired = dtf.parse(loginTicket.getExpired());
            if (loginTicket != null && loginTicket.getStatus() == 0 && expired.after(new Date())) {
                // 根据凭证 ticket 查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户，在多个线程中隔离对象
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 得到当前线程的 User
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
