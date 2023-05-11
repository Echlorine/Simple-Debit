package xyz.echocolate.debit.util;

/**
 * @Description: Redis 键工具类
 * @ClassName: RedisKeyUtil
 * @Author Cmite
 * @Date: 2022/7/8 12:25
 * @Version 1.0
 */

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_TICKET = "ticket";

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }
}
