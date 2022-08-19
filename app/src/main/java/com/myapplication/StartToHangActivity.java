package com.myapplication;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.dto.mo.SlaughterInfo;
import com.myapplication.utils.ActivityHelper;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.RabbitMqDataReceiver;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 起挂Activity
 *
 * @author mijiahao
 * @date 2022/08/13
 */
public class StartToHangActivity extends FragmentActivity {



     // -----组件变量区-----------------------------------------------//

    private TitleView titleView;
    private EditText etBatchCode;
    private EditText etSlaughterWeight;
    private EditText etSlaughterDate;
    private EditText etRfid;
    private EditText etCardCode;



     // -----全局变量区-----------------------------------------------//

    /**
     * 起挂信息的数据模型，更新UI前请更新数据模型
     */
    private SlaughterInfo globalSlaughterInfo = new SlaughterInfo();
    /**
     * 芯片ID接收器
     */
    private final RabbitMqDataReceiver rabbitMqDataReceiver = new RabbitMqDataReceiver();
    /**
     * 芯片ID handler
     */
    private final RfidHandler rfidHandler = new RfidHandler();

    private class RfidHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            activityHelper.setText(etRfid,msg.obj.toString());
        }
    }
    private ActivityHelper activityHelper = new ActivityHelper(this);


     // -----常量区-----------------------------------------------//

    private final String ACTIVITY_TITLE = "起挂";
    /**
     * url，查询一个批次中最先需要起挂的一条记录
     */
    private final String URL_GET_SLAUGHTER_INFO = StringUrl.GetUrl() + "/api/mo/slaughterinfo/getEarliestSlaughterInfo" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_to_hang);
        init();
        /**
         * DATE: 2022/8/16
         * mijiahao TODO: 芯片ID如何来接收？
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rabbitMqDataReceiver.closeConnection();
    }

    /**
     * 初始化
     */
    private void init(){
        initUiComponents();
        initBatchCode();
        initSlaughterInfo();
    }

    /**
     * 初始化ui组件
     */
    private void initUiComponents(){
        etBatchCode = findViewById(R.id.et_batch_code);
        etSlaughterDate = findViewById(R.id.et_slaughter_date);
        etSlaughterWeight = findViewById(R.id.et_slaughter_weight);
        etRfid = findViewById(R.id.et_rfid);
        etCardCode = findViewById(R.id.et_card_code);
        titleView = findViewById(R.id.titleView);
        titleView.setAppTitle(ACTIVITY_TITLE);
        titleView.setLeftImgOnClickListener();
    }

    /**
     * 初始化批次信息
     */
    private void initBatchCode () {
        SlaughterInfo info = (SlaughterInfo) this.getIntent().getSerializableExtra("info");
        if (info == null) {
            return;
        }
        etBatchCode.setText(info.getBatchCode());
        globalSlaughterInfo.setBatchCode(info.getBatchCode());
    }

    /**
     * 初始化起挂数据,查询一个批次中最先需要起挂的一条记录
     */
    private void initSlaughterInfo() {
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("batchCode", globalSlaughterInfo.getBatchCode());
        OkHttpUtils.postAsyn(URL_GET_SLAUGHTER_INFO,queryMap,new HttpCallback(){
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                if (resultDesc.getsuccess()){
                    String res=resultDesc.getresult().toString();
                    if (resultDesc.getresult().equals("null")){
                        /**
                         * DATE: 2022/8/15
                         * mijiahao TODO: 空的该怎么办？
                         */
                        activityHelper.alertDialog("查询不到批次为\""+globalSlaughterInfo.getBatchCode()+"\"的屠宰信息！");
                        return;
                    }
                    updateSlaughterInfo(JSON.parseObject(res, SlaughterInfo.class));;
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
            }
        });
    }

    /**
     * 更新屠宰信息
     *
     * @param slaughterInfo 屠宰信息
     */
    private void updateSlaughterInfo(SlaughterInfo slaughterInfo){
        if (slaughterInfo == null){
            activityHelper.alertDialog("更新数据模型不能为空");
            return;
        }
        globalSlaughterInfo = slaughterInfo;
        refreshView();
    }

    /**
     * 刷新视图
     */
    private void refreshView(){
       activityHelper.setText(etBatchCode,globalSlaughterInfo.getBatchCode());
       activityHelper.setText(etSlaughterWeight,globalSlaughterInfo.getSlaughterWeight().toString());
       activityHelper.setText(etSlaughterDate,globalSlaughterInfo.getSlaughterDate());
       activityHelper.setText(etRfid,globalSlaughterInfo.getRfid());
    }


}