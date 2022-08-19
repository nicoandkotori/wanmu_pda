package com.myapplication.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Vfun01 on 2017-11-03.
 */

public class MyDatePickerDialog extends DatePickerDialog  {

     public MyDatePickerDialog (Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
       super(context, callBack, year, monthOfYear, dayOfMonth);



        this.setTitle("请选择日期");

         this.setCancelable(true);
         this.setCanceledOnTouchOutside(true);
         this.setButton(DialogInterface.BUTTON_POSITIVE, "确认", this);
         this.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
//         this.setButton(DialogInterface.BUTTON_NEUTRAL, "清空",(OnClickListener)null);

         this.setButton(DialogInterface.BUTTON_NEUTRAL, "清空", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 onDateChanged(null,1900,0,1);
                 MyDatePickerDialog.super.onClick(dialog,DialogInterface.BUTTON_POSITIVE);

//                MyDatePickerDialog.super.onClick(dialog,which);
             }
         });
//        this.setButton2("取消", (OnClickListener)null);
//        this.setButton("确定", this);  //setButton和this参数组合表示这个按钮是确定按钮
//        // 获取当前系统的语言
//          Locale locale = context.getResources().getConfiguration().locale;
//          String language = locale.getLanguage();
//         // 隐藏日选择栏
//         if (language.endsWith("zh")) {
//                 ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
//                         .getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
//             } else {
//                 ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
//                          .getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
//         }

    }

     @Override
     public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);
                this.setTitle("请选择日期");
     }

     @Override
     public void onStop() {

     }
}

