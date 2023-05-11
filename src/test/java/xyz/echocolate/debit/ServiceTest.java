package xyz.echocolate.debit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import xyz.echocolate.debit.entity.Expenses;
import xyz.echocolate.debit.service.ExpensesService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ServiceTest
 * @Description 测试Service
 * @Author echlorine
 * @Date 2023/4/17 21:09
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = DebitApplication.class)
public class ServiceTest {
    @Autowired
    private ExpensesService expensesService;

    @Test
    public void testSum() {
        double d = expensesService.sumByMonth("2023-04");
        System.out.println(d);
    }

    @Test
    public void testGetDetail() {
        String[] details = expensesService.getDetailByDay("2023-04-20");
        for (int i = 0; i < details.length; i++) {
            System.out.println(details[i]);
        }
    }

}
