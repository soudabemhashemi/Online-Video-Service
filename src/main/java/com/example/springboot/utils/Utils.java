package com.example.springboot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String quoteWrapper(String str) {
        return "'" + str + "'";
    }
    public static String convertToString(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static LocalDateTime convertToLocalDateTime(String time) {
        if (time == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    public static String searchKeywordWrapper(String str) { return "'%" + str + "%'"; }
}
