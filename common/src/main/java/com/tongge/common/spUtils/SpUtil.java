package com.tongge.common.spUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



/**
 * Created by Administrator on 2016/4/5.
 */
public class SpUtil {
    static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }



    public static void setLanguage(String language) {
        prefs.edit().putString("language", language).commit();
    }
    public static String getLanguage() {
        return prefs.getString("language","zh-CN");
    }


    public static void setLastTime(Long lastTime) {
        prefs.edit().putLong("lastTime", lastTime).commit();
    }
    public static Long getLastTime() {
        return prefs.getLong("lastTime",0);
    }




}
