package xyz.echocolate.debit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import xyz.echocolate.debit.annotation.LoginRequired;
import xyz.echocolate.debit.entity.CalendarDay;
import xyz.echocolate.debit.entity.Expenses;
import xyz.echocolate.debit.service.ExpensesService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName HomeController
 * @Description 渲染主页
 * @Author echlorine
 * @Date 2023/4/16 14:35
 * @Version 1.0
 */
@Controller
public class HomeController {
    @Autowired
    private ExpensesService expensesService;

    @LoginRequired
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model) {
        String[] weeks = new String[]{
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六",
                "星期日"
        };
        List<List<CalendarDay>> list = genCalendartable();
        String dayArr = genDayArr();
        // 确定当前月份与当前天数
        LocalDate curDate = LocalDate.now();
        int curDayOfWeek = curDate.getDayOfWeek().getValue();
        int curYear = curDate.getYear();
        String curMonth = String.format("%02d月", curDate.getMonthValue());
        String curDay = String.format("%02d", curDate.getDayOfMonth());
        String today_date_id = "date";
        if (curDayOfWeek >= 5) {
            curDay += "_2";
            today_date_id += "_2";
        }
        String today_day = curYear + "年" + curMonth;
        String today_week_id = weeks[curDayOfWeek - 1];
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = curDate.format(sdf);
        double sumByDay = expensesService.sumByDay(date);
        double sumByMonth = expensesService.sumByMonth(date);
        double sumByYear = expensesService.sumByYear(date);
        String[] details = expensesService.getDetailByDay(date);
        // 月份列表
        String[] monthList = new String[12];
        for (int i = 0; i < 12; i++) {
            monthList[i] = String.format("%02d", i + 1);
        }
        // 年份列表
        String[] yearList = new String[] {String.valueOf(curYear), String.valueOf(curYear - 1), String.valueOf(curYear - 2)};
        // 添加相应属性
        model.addAttribute("calendarList", list);
        model.addAttribute("dayArr", dayArr);
        model.addAttribute("today_day", today_day);
        model.addAttribute("today_date_id", today_date_id);
        model.addAttribute("today_week_id", today_week_id);
        model.addAttribute("day", curDay);
        model.addAttribute("today", new Date());
        model.addAttribute("curDate", date);
        model.addAttribute("amount_day", "￥ " + sumByDay);
        model.addAttribute("amount_month", "￥ " + sumByMonth);
        model.addAttribute("amount_year", "￥ " + sumByYear);
        model.addAttribute("details", details);
        model.addAttribute("monthList", monthList);
        model.addAttribute("yearList", yearList);
        return "index";
    }

    @LoginRequired
    @RequestMapping(path = "/queryAll", method = RequestMethod.POST)
    @ResponseBody
    private String queryAll(String day) {
        double amountDay  = expensesService.sumByDay(day);
        double amountMonth  = expensesService.sumByMonth(day);
        double amountYear  = expensesService.sumByYear(day);
        String[] detail = expensesService.getDetailByDay(day);
        Map<String, Object> map = new HashMap<>();
        map.put("amountDay", "￥ " + amountDay);
        map.put("amountMonth", "￥ " + amountMonth);
        map.put("amountYear", "￥ " + amountYear);
        map.put("detail", detail);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        // System.out.println(json);
        return json;
    }

    @LoginRequired
    @RequestMapping(path = "/queryByMonth", method = RequestMethod.POST)
    @ResponseBody
    public String getMonthDebit(String month) {
        double amountMonth = expensesService.sumByMonth(month);
        Map<String, Object> map = new HashMap<>();
        map.put("amountMonth", "￥ " + amountMonth);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        // System.out.println(json);
        return json;
    }

    @LoginRequired
    @RequestMapping(path = "/queryByYear", method = RequestMethod.POST)
    @ResponseBody
    public String getYearDebit(String year) {
        double amountYear = expensesService.sumByYear(year);
        Map<String, Object> map = new HashMap<>();
        map.put("amountYear", "￥ " + amountYear);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        // System.out.println(json);
        return json;
    }
    @LoginRequired
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDayDebit(String amount, String description, String day) {
        int res = add(amount, description, day);
        if (res > 0) {
            return queryAll(day);
        }
        return "error";
    }

    @LoginRequired
    @RequestMapping(path = "/addTest", method = RequestMethod.POST)
    public String addDayDebitTest(@RequestParam("amount") String amount,
                                  @RequestParam("description") String description,
                                  @RequestParam("todayDate") String day) {
        int res = add(amount, description, day);
        if (res > 0) {
            return "redirect:/";
        }
        return "error";
    }

    private int add(String amount, String description, String day) {
        Expenses expenses = new Expenses();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        expenses.setAmount(new BigDecimal(amount));
        expenses.setDescription(description);
        try {
            expenses.setDate(sdf.parse(day));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int i = expensesService.insertDebit(expenses);
        return i;
    }

    /**
     * 确定日历的第一天和最后一天
     * @return
     */
    private LocalDate[] getFL() {
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

    /**
     * 生成日历表
     */
    private List<List<CalendarDay>> genCalendartable() {
        // 确定当前月份与当前天数
        LocalDate curDate = LocalDate.now();
        int curMonth = curDate.getMonthValue();
        LocalDate[] FL = getFL();
        int tag = 100 - (int) (LocalDate.now().toEpochDay() - FL[0].toEpochDay());
        int weekNum = (int) (FL[1].toEpochDay() - FL[0].toEpochDay()) / 7;
        List<List<CalendarDay>> list = new ArrayList<>();
        String[] weekOfDay = new String[]{"mon", "tues", "wed", "thur", "fri", "sat", "sun"};
        for (int i = 0; i < weekNum; i++) {
            List<CalendarDay> ans = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                // 确定每个元素的 tag, day
                CalendarDay calendarDay = new CalendarDay();
                int diff = i * 7 + j;
                int month = FL[0].plusDays(diff).getMonthValue();
                int day = FL[0].plusDays(diff).getDayOfMonth();

                int monthDiff = month - curMonth;
                int dayDiff = (int) (FL[0].plusDays(diff).toEpochDay() - curDate.toEpochDay());

                int monthTag = 1;
                if (diff == 0) monthTag = 0; // 日历的第一天
                if (diff == weekNum * 7 - 1) monthTag = 2; // 日历的最后一天

                StringBuffer sb = new StringBuffer("riliday");
                if (monthDiff != 0) sb.append(" noyue");
                if (dayDiff == 0) sb.append(" today");
                if (dayDiff == -1) sb.append(" yesterday");
                if (dayDiff == 1) sb.append(" tomorrow");
                sb.append(" " + weekOfDay[j]);
                // 设置元素属性
                calendarDay.setTdClass(sb.toString());
                calendarDay.setTag(tag + diff);
                calendarDay.setDay(day);
                calendarDay.setDayTag(dayDiff);
                calendarDay.setMonthTag(monthTag);
                ans.add(calendarDay);
            }
            list.add(ans);
        }
        return list;
    }

    /**
     * 生成日历JSON数组
     */
    private String genDayArr() {
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
                map.put("day", String.format("%02d", day));
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
        return json;
    }
}
