package xyz.echocolate.debit.entity;

import lombok.Data;

/**
 * @ClassName CalendarDay
 * @Description 日历表格中单个日期实体
 * @Author echlorine
 * @Date 2023/4/22 16:38
 * @Version 1.0
 */
@Data
public class CalendarDay {
    private String tdClass;

    private int tag;

    private int day;

    private int dayTag;

    private int monthTag;
}
