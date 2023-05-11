package xyz.echocolate.debit.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.echocolate.debit.entity.Expenses;

import java.util.List;

/**
 * @ClassName ExpensesMapper
 * @Description Expenses表的数据访问接口
 * @Author echlorine
 * @Date 2023/4/16 12:27
 * @Version 1.0
 */
@Mapper
public interface ExpensesMapper {
    Expenses selectById(int id);

    List<Expenses> selectByDay(String day);

    List<Expenses> selectByMonth(String month);

    List<Expenses> selectByYear(String year);

    int insertDebit(Expenses expenses);
}
