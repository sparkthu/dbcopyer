package com.sohu.compass.dbcopyer.executor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Template {
    public static String replace(String sql) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(Calendar.getInstance().getTime());
        return sql.replace("{{.TODAY}}", today);
    }
}
