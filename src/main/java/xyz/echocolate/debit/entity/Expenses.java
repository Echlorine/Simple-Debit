package xyz.echocolate.debit.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName Expenses
 * @Description 支出实体类
 * @Author echlorine
 * @Date 2023/4/16 12:13
 * @Version 1.0
 */
@Data
public class Expenses {
    private int id;
    private Date date;
    private BigDecimal amount;
    private String description;
}
