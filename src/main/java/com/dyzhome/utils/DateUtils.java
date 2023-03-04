package com.dyzhome.utils;

import cn.hutool.core.util.ObjectUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期处理
 *
 * @author Dyz
 */
public class DateUtils {
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(LocalDateTime date) {
        return format(date, DATE_PATTERN);
    }

    public static String formatDateTime(LocalDateTime date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static String format(LocalDateTime date, String pattern) {
        if (ObjectUtil.isNotNull(date)) {
            return DateTimeFormatter.ofPattern(pattern).format(date);
        }
        return null;
    }
}
