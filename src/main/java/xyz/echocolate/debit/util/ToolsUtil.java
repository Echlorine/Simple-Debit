package xyz.echocolate.debit.util;

import org.springframework.util.DigestUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * @Description: 基础工具类
 * @ClassName: ToolsUtil
 * @Author Cmite
 * @Date: 2022/6/28 10:45
 * @Version 1.0
 */

public class ToolsUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密，撒盐加密
    public static String md5(String key) {
        if (Objects.isNull(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
