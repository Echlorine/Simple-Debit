package xyz.echocolate.debit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import xyz.echocolate.debit.dao.ExpensesMapper;
import xyz.echocolate.debit.entity.Expenses;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MapperTest
 * @Description 测试Mapper
 * @Author echlorine
 * @Date 2023/4/16 14:02
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = DebitApplication.class)
public class MapperTest {

    @Autowired
    private ExpensesMapper expensesMapper;

    @Test
    public void testSelectExpenses() {
        Expenses user = expensesMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testSelectByDate() {
        List<Expenses> list = expensesMapper.selectByMonth("2023-04");
        for (Expenses e: list) {
            System.out.println(e.toString());
        }
    }
}

