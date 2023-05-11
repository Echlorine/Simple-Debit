package xyz.echocolate.debit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.echocolate.debit.dao.ExpensesMapper;
import xyz.echocolate.debit.entity.Expenses;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ExpensesService
 * @Description 处理业务逻辑
 * @Author echlorine
 * @Date 2023/4/16 14:26
 * @Version 1.0
 */
@Service
public class ExpensesService {
    @Autowired
    private ExpensesMapper expensesMapper;

    public int insertDebit(Expenses expenses) {
        int res = expensesMapper.insertDebit(expenses);
        return res;
    }

    public double sumByDay(String day) {
        List<Expenses> list = expensesMapper.selectByDay(day);
        BigDecimal res = new BigDecimal("0.00");
        for (Expenses e : list) {
            res = res.add(e.getAmount());
        }
        return res.doubleValue();
    }

    public double sumByMonth(String month) {
        List<Expenses> list = expensesMapper.selectByMonth(month);
        BigDecimal res = new BigDecimal("0.00");
        for (Expenses e : list) {
            res = res.add(e.getAmount());
        }
        return res.doubleValue();
    }

    public double sumByYear(String year) {
        List<Expenses> list = expensesMapper.selectByYear(year);
        BigDecimal res = new BigDecimal("0.00");
        for (Expenses e : list) {
            res = res.add(e.getAmount());
        }
        return res.doubleValue();
    }

    public String[] getDetailByDay(String day) {
        List<Expenses> list = expensesMapper.selectByDay(day);
        String[] detailAll = new String[list.size()];
        for (int i = 0; i < detailAll.length; i++) {
            // 麦当劳 · ￥25.25
            detailAll[i] = list.get(i).getDescription() + " · ￥" + list.get(i).getAmount();
        }
        return detailAll;
    }
}
