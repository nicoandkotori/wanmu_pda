package com.myapplication.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * 封装Activity公用方法的工具类
 *
 * @author mijiahao
 * @date 2022/08/15
 */
public class ActivityHelper {
    
    Activity activity;

    public ActivityHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 设置文本
     *
     * @param view  视图
     * @param value 值
     */
    public void setText(TextView view, String value){
        if (!StringUtil.isEmpty(value)){
            view.setText(value);
        }
    }

    /**
     * 设置文本
     *
     * @param view  视图
     * @param value 值
     */
    public void setText(TextView view, Date value){
        if (value != null){
            view.setText(DateUntil.getChineseDate(value));
        }
    }

    /**
     * 显示Toast
     *
     * @param test 测试
     */
    public void showToast(String test){
        Toast.makeText(activity.getApplicationContext(),test,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示警告对话框
     *
     * @param text 文本
     */
    public void alertDialog(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(text);
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
