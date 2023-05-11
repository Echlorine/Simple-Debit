package xyz.echocolate.debit.util;

import org.springframework.stereotype.Component;
import xyz.echocolate.debit.entity.User;

/**
 * @Description: 持有用户信息，用于代替session对象
 * @ClassName: HostHolder
 * @Author Cmite
 * @Date: 2022/6/30 13:26
 * @Version 1.0
 */

@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
