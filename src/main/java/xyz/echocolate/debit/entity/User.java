package xyz.echocolate.debit.entity;

import lombok.Data;

/**
 * @Description: 用户实体类
 * @ClassName: User
 * @Author Cmite
 * @Date: 2022/6/27 12:13
 * @Version 1.0
 */

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
}
