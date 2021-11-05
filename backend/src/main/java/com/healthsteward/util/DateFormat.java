package com.healthsteward.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    /**
     * get Date from String with Custom Pattern
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String date, String pattern) throws ParseException {
        // "yyyy-MM-dd hh:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    /**
     * get Date from String with Fixed Pattern
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    /**
     * get String of Date with Pattern
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern)  {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * transform Date Pattern
     * @param date
     * @param originPattern
     * @param targetPattern
     * @return
     * @throws ParseException
     */
    public static Date transform(Date date, String originPattern, String targetPattern) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat(originPattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(targetPattern);
        String tmp = sdf1.format(date);
        return sdf2.parse(tmp);
    }

}
