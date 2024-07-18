package com.li.util;

import java.text.SimpleDateFormat;

public class DatetimeUtils {
    public static String timestampToDate(long timestamp) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(timestamp);
    }
}
