/*
 * Copyright  2017 [AllenCoderr]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongge.common.sysUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Stack;

/**
 * 应用程序Activity管理工具类，用于Activity的管理和应用程序的退出
 */
public class AppManger {
    public static boolean isDebug;
    private static Stack<Activity> activityStack;
    public static final AppManger getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private AppManger() {}

    private static class SingletonHolder{
        private static final AppManger INSTANCE = new AppManger();
    }

    /**
     * 添加Activity 到栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity();
            }
        }
    }

    /**
     * 结束所有的Activity、
     */
    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }

    /**
     * 判断App是否是Debug版本
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug(Context context, String packageName) {
        boolean isDebug;
        if (TextUtils.isEmpty(packageName)) isDebug = true;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            isDebug = ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isDebug = true;
        }
        Log.i("【 app is debug 】","▂▃▃▃▅▆▇▉ "+ isDebug);
        return isDebug;
    }
}
