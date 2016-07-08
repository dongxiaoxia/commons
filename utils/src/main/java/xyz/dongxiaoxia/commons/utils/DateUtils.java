package xyz.dongxiaoxia.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author dongxiaoxia
 * @create 2016-07-06 17:40
 */
public class DateUtils {
    /**
     * 根据日期获取星期几
     *
     * @param date 日期
     * @return 星期
     */
    public static String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    public static String getAge(Date date) {
        Calendar birthDay = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        birthDay.setTime(date);
        if (now.before(birthDay)) {
            throw new IllegalArgumentException("The birthday is before Now.It's unbelievable");
        }
        int day = now.get(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) - birthDay.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if (day < 0) {
            month -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上一个月的天数。
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (month < 0) {
            month = (month + 12) % 12;
            year--;
        }
       return year + "岁" + month + "个月" + day + "天";
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getWeek(new Date()));
        System.out.println(getAge(new SimpleDateFormat("yyyy-MM-dd").parse("1991-10-03")));
    }
}
