package xyz.echocolate.debit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.echocolate.debit.entity.LoginTicket;
import xyz.echocolate.debit.entity.User;
import xyz.echocolate.debit.util.RedisKeyUtil;
import xyz.echocolate.debit.util.ToolsUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName UserService
 * @Description 处理用户登录逻辑
 * @Author echlorine
 * @Date 2023/5/7 20:26
 * @Version 1.0
 */
@Service
public class UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    
    private User generateUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Yuki");
        user.setPassword("WgVaV.-+m29U!Mb");
        user.setSalt("security");
        user.setPassword(ToolsUtil.md5(user.getPassword() + user.getSalt()));
        return user;
    }

    public LoginTicket login(String username, String password, int expiredSeconds) {
        // 处理空值
        if (Objects.isNull(username) || Objects.isNull(password)) {
            return null;
        }

        // 此处获取User实体没有查表，而是写死了
        User user = generateUser();
        // 验证账号密码是否匹配
        password = ToolsUtil.md5(password + user.getSalt());
        if (!user.getUsername().equals(username) || !user.getPassword().equals(password)) {
            return null;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(ToolsUtil.generateUUID());
        loginTicket.setStatus(0); // 表示登录状态有效
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
        loginTicket.setExpired(dtf.format(new Date(System.currentTimeMillis() + expiredSeconds * 1000)));
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        return loginTicket;
    }

    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    public User findUserById(int userId) {
        return generateUser();
    }
}
