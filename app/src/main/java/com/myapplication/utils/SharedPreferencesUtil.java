package com.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存数据应用 配置的集合类
 */
public class SharedPreferencesUtil {
    public static final String SETTING_FILE = "server-info";

    /**
     * 保存设置项到文件
     */
    public static void saveToFile(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SETTING_FILE,
                android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 删除
     */
    public static void deleteToFile(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SETTING_FILE,
                android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean isContains(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SETTING_FILE,
                android.content.Context.MODE_PRIVATE);
        if (pref.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设置项
     */
    public static String getFromFile(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SETTING_FILE,
                android.content.Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }
}
