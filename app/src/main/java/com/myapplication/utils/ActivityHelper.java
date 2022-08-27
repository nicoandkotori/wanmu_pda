package com.myapplication.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication.HttpClient.ResultDesc;

import java.math.BigDecimal;
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
     * TextView设置文本
     *
     * @param view  视图
     * @param value 值
     */
    public void setText(TextView view, String value){
        if (!StringUtil.isEmpty(value)){
            //内容相同则不更新
            if (!value.contentEquals(view.getText())){
                view.setText(value);
            }

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
     * 设置文本
     *
     * @param view  视图
     * @param value 值
     */
    public void setText(TextView view, BigDecimal value){
        if (value != null){
            view.setText(value.toString());
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

    /**
     * 判断http请求响应数据是否为空
     *
     * @param resultDesc 结果desc
     * @return boolean
     */
    public boolean isResultDataEmpty(ResultDesc resultDesc){
        if (resultDesc.getresult() == null){
            return true;
        }
        if (StringUtil.isEmpty(resultDesc.getresult().toString())){
            return true;
        }
        return false;
    }
}
