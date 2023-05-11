package xyz.echocolate.debit.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description: 登录凭证实体类
 * @ClassName: LoginTicket
 * @Author Cmite
 * @Date: 2022/6/29 10:39
 * @Version 1.0
 */

@Data
public class LoginTicket implements Serializable {


    @Serial
    private static final long serialVersionUID = 7505658557411063823L;
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private String expired;

    @Override
    public String toString() {
        return "LoginTicket#" +
                "id=" + id +
                "&userId=" + userId +
                "&ticket='" + ticket + "'" +
                "&status=" + status +
                "&expired=" + expired +
                '#';
    }
}
