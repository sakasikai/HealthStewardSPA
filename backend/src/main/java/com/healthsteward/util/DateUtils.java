package com.healthsteward.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    //检查日期是否为星期一
    public static boolean isMonday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
    }
}
