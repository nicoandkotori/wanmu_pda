package com.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.app.Fragment;
import android.widget.TimePicker;
import android.widget.Toast;

import com.myapplication.utils.Constant;
import com.myapplication.utils.MyDatePickerDialog;
import java.util.Calendar;
/**
 * Created by Vfun01 on 2017-11-03.
 */

public   class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int _year=1970;
    int _month=0;
    int _day=0;
         @Override
     public   Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
             int year = c.get(Calendar.YEAR);
             int month = c.get(Calendar.MONTH);
             int day = c.get(Calendar.DAY_OF_MONTH);
               return new MyDatePickerDialog(getActivity(), this, year, month, day);
             }

            @Override
    public  void onDateSet(DatePicker view, int year, int month, int day) {
                _year=year;
                _month=month + 1;
                _day=day;
                getValue();
                Log.d("DateSet","选择的日期是：" + year +"-" + (month + 1) + "-" + day);
    }
    public  String getValue(){
        return ""+_year+_month+_day;
    }

}

