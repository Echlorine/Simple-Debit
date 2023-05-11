package xyz.echocolate.debit.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.echocolate.debit.entity.LoginTicket;
import xyz.echocolate.debit.service.UserService;

import java.util.Objects;

/**
 * @ClassName LoginController
 * @Description 登录功能
 * @Author echlorine
 * @Date 2023/5/7 20:18
 * @Version 1.0
 */

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpServletResponse resp) {
        // 检查账号，密码
        int expiredSeconds = 3600 * 12;
        LoginTicket loginTicket = userService.login(username, password, expiredSeconds);
        if (Objects.nonNull(loginTicket)) {
            Cookie cookie = new Cookie("ticket", loginTicket.getTicket());
            cookie.setPath("");
            cookie.setMaxAge(expiredSeconds);
            resp.addCookie(cookie);
            return "redirect:/";
        } else {
            return "login";
        }
    }
}