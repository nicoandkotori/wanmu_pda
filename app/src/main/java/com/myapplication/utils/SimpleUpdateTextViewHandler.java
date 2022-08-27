package com.myapplication.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 用于更新TextView的handler
 *
 * @author mijiahao
 * @date 2022/08/25
 */
public class SimpleUpdateTextViewHandler extends Handler {

    TextView textView;

    public SimpleUpdateTextViewHandler(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void handleMessage(Message msg) {
        textView.setText(msg.obj.toString());
    }
}
