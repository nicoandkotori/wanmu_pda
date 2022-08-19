package com.myapplication.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by Vfun01 on 2017-11-06.
 */

public class DatePickDialogUtil {

    Context context;
    DatePickerDialog.OnDateSetListener callBack;
    public DatePickDialogUtil(Context context, DatePickerDialog.OnDateSetListener callBack)
    {
        this.context=context;
        this.callBack=callBack;
    }

    public Dialog onCreateDialog() {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
        return  new MyDatePickerDialog(context,callBack,year,month,day);

    }

}