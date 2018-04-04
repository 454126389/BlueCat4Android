package com.tongge.common.sysUtils;

import android.util.Log;

/**
 * Created by DZ on 2017/5/3.
 */

public class STLog {
    private STLog() {
    }

    public static void d(Object obj, String msg) {
        if (AppManger.isDebug) Log.d(analyzeTag(obj), formatMsg(msg));
    }

    public static int d(Object obj, String msg, Throwable tr) {
        return Log.d(analyzeTag(obj), formatMsg(msg), tr);
    }

    public static void e(Object obj, String msg) {
        if (AppManger.isDebug) Log.e(analyzeTag(obj), formatMsg(msg));
    }

    public static void v(Object obj, String msg) {
        if (AppManger.isDebug) Log.v(analyzeTag(obj), formatMsg(msg));
    }

    public static void i(Object obj, String msg) {
        if (AppManger.isDebug) Log.i(analyzeTag(obj), formatMsg(msg));
    }

    private static String formatTag(String old) {
        return "【 " + old + " 】";
    }

    private static String formatMsg(String old) {
        return "▂▃▃▃▅▆▇▉ " + old;
    }

    private static String analyzeTag(Object obj){
        String tag ;
        if (obj instanceof String)
            tag = (String) obj;
        else
            tag = obj.getClass().getSimpleName();
        return formatTag(tag);
    }
}
