package com.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.dto.System.ServerInfo;

import com.myapplication.utils.CommonAdapter;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.CustomStringUtils;
import com.myapplication.utils.SharedPreferencesUtil;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.util.ArrayList;
import java.util.List;

public class ServerSetupActivity extends FragmentActivity {

    private TitleView titleView;
    private Button mbtnSave;//保存
    private Button mbtnCanel;//取消


    private EditText ip;
    private EditText port;
    private EditText printer;
    private int IsSave=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setup);
        //标题
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("服务器配置");
        titleView.setLeftImgOnClickListener();

        //主表控件获取
        ip = (EditText) findViewById(R.id.ip);
        printer = (EditText) findViewById(R.id.printer);
        port = (EditText) findViewById(R.id.port);
        mbtnSave=(Button)findViewById(R.id.btnSave);
        mbtnCanel=(Button)findViewById(R.id.btnCanel);
        ip.setText(StringUrl.getIp());
        port.setText(StringUrl.getPort());
        printer.setText(StringUrl.getPrinter());



        //更新数据
        final Context applicationContext = getApplicationContext();
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IsSave = 1;
                    if(StringUtil.isEmpty(ip.getText().toString()))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServerSetupActivity.this);
                        builder.setMessage("请输入正确的IP地址!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if(StringUtil.isEmpty(port.getText().toString()))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServerSetupActivity.this);
                        builder.setMessage("请输入正确的端口号!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
//                    if(StringUtil.isEmpty(printer.getText().toString()))
//                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(ServerSetupActivity.this);
//                        builder.setMessage("请输入正确的打印机名!");
//                        builder.setPositiveButton("确定",null);
//                        builder.show();
//                        IsSave=0;
//                        return;
//                    }
                    StringUrl.setPrinter(printer.getText().toString());
                    StringUrl.setIp(ip.getText().toString());
                    StringUrl.setPort(port.getText().toString());
                    //保存到手机
                    SharedPreferencesUtil.deleteToFile(applicationContext,"ip");
                    SharedPreferencesUtil.deleteToFile(applicationContext,"port");
                    SharedPreferencesUtil.deleteToFile(applicationContext,"printer");
                    SharedPreferencesUtil.saveToFile(applicationContext,"ip",ip.getText().toString());
                    SharedPreferencesUtil.saveToFile(applicationContext,"port",port.getText().toString());
                    SharedPreferencesUtil.saveToFile(applicationContext,"printer",printer.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServerSetupActivity.this);
                    builder.setMessage("保存成功" );
                    builder.setPositiveButton("确定", null);
                    builder.show();
                } catch (Exception e) {
                    IsSave = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ServerSetupActivity.this);
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("确定", null);
                    builder.show();
                }
            }
        });

        mbtnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    ip.setText("");
                    port.setText("");
                    printer.setText("");
                } catch (Exception e) {

                }

            }
        });



    }


}
