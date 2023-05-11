package xyz.echocolate.debit;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @ClassName AlphaTest
 * @Description 测试类
 * @Author echlorine
 * @Date 2023/4/17 21:28
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = DebitApplication.class)
public class AlphaTest {
    @Test
    public void genDayArr() {
        String[] weeks = new String[]{
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六",
                "星期日"
        };
        // 生成 JSON 数据
        LocalDate[] FL = getFL();
        int tag = 100 - (int) (LocalDate.now().toEpochDay() - FL[0].toEpochDay());
        List<Map<String, String>> list = new ArrayList<>();
        int weekNum = (int) (FL[1].toEpochDay() - FL[0].toEpochDay()) / 7;
        for (int i = 0; i < weekNum; i++) {
            // 构建从星期一到星期日
            for (int j = 0; j < 7; j++) {
                Map<String, String> map = new HashMap<>();
                int diff = i * 7 + j;
                int day = FL[0].plusDays(diff).getDayOfMonth();
                int month = FL[0].plusDays(diff).getMonthValue();
                int year = FL[0].plusDays(diff).getYear();
                map.put("tag", String.valueOf(tag + diff));
                map.put("day", String.valueOf(day));
                map.put("month", String.format("%02d", month));
                map.put("year", String.valueOf(year));
                map.put("weekOfDay", weeks[j]);
                if (j >= 5) {
                    map.put("isWeekend", "_2");
                } else {
                    map.put("isWeekend", "");
                }
                list.add(map);
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);
    }

    public LocalDate[] getFL() {
        // 当月的总天数
        int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        // 确定当天，当月第一天、最后一天是周几
        LocalDate curDate = LocalDate.now();
        LocalDate firstDateOfMonth =  LocalDate.of(curDate.getYear(), curDate.getMonth(), 1);
        LocalDate lastDateOfMonth =  LocalDate.of(curDate.getYear(), curDate.getMonth(), maxDay);
        int firstWeekOfDay = firstDateOfMonth.getDayOfWeek().getValue(); // 第一天
        int lastWeekOfDay = lastDateOfMonth.getDayOfWeek().getValue(); // 最后一天
        // 确定日历的第一个日期和最后一个日期
        int preDays = firstWeekOfDay == 1 ? 7 : firstWeekOfDay - 1;
        int nextDays = lastWeekOfDay == 7 ? 7 : 7 - lastWeekOfDay;
        // 确定日期应该展现的总星期数
        int daysNum = maxDay + preDays + nextDays > 35 ? 42 : 35;
        LocalDate firstDate = curDate.minusDays(curDate.getDayOfMonth() - 1 + preDays);
        LocalDate lastDate = firstDate.plusDays(daysNum);
        return new LocalDate[] {firstDate, lastDate};
    }

    @Test
    public void genCalendartable() {
        // 确定当前月份与当前天数
        LocalDate curDate = LocalDate.now();
        int curMonth = curDate.getMonthValue();
        int curDay = curDate.getDayOfMonth();
        LocalDate[] FL = getFL();
        int tag = 100 - (int) (LocalDate.now().toEpochDay() - FL[0].toEpochDay());
        int weekNum = (int) (FL[1].toEpochDay() - FL[0].toEpochDay()) / 7;
        List<List<Map<String, String>>> list = new ArrayList<>();
        for (int i = 0; i < weekNum; i++) {
            List<Map<String, String>> ans = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                // 确定每个元素的 tag, day
                Map<String, String> map = new HashMap<>();
                int diff = i * 7 + j;
                int month = FL[0].plusDays(diff).getMonthValue();
                int monthTag = month - curMonth;
                int day = FL[0].plusDays(diff).getDayOfMonth();
                int dayTag = monthTag == 0 ? day - curDay : 100;
                map.put("tag", String.valueOf(tag + diff));
                map.put("day", String.valueOf(day));
                map.put("monthTag", String.valueOf(monthTag));
                map.put("dayTag", String.valueOf(dayTag));
                ans.add(map);
            }
            list.add(ans);
        }
        System.out.println(list);
    }
}
