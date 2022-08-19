package com.myapplication.utils;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 销毁Activity util，使用该工具类添加需要被销毁的Activity，并能在任何地方销毁该Activity
 *
 * @author mijiahao
 * @date 2022/08/09
 */
public class DestroyActivityUtil {

    private static Map<String, Activity> destoryMap = new HashMap<>();

    //将Activity添加到队列中
    public static void addDestroyActivityToMap(String activityName,Activity activity) {
        destoryMap.put(activityName, activity);
    }

    //根据名字销毁制定Activity
    public static void destroyActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destoryMap.get(key).finish();
                    destoryMap.remove(key);
                    Log.d("DestroyActivityUtil",activityName+"销毁成功!");
                }
            }
        }
    }

}
