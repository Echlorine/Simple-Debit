package xyz.echocolate.debit.util;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: Cookie工具类
 * @ClassName: CookieUtil
 * @Author Cmite
 * @Date: 2022/6/30 12:58
 * @Version 1.0
 */

public class CookieUtil {
    public static String getValue(HttpServletRequest req, String name) {
        if (req == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
