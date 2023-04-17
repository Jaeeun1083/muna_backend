package com.life.muna.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    private static final String DATE_TIME_FORMAT = "yyyy.MM.dd HH:mm:ss";

    public static String convert(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        return formatter.format(date);
    }

}
