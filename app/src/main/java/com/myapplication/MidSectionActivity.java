package com.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.dto.mo.MidSectionInfo;
import com.myapplication.dto.mo.MidSectionInfoDTO;
import com.myapplication.dto.mo.SlaughterInfoDTO;
import com.myapplication.utils.ActivityHelper;
import com.myapplication.utils.EnumUtils;
import com.myapplication.utils.RabbitMqDataReceiver;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidSectionActivity extends FragmentActivity {


    // -----组件变量区-----------------------------------------------//

    private TitleView titleView;
    private EditText etBatchCode;
    private EditText etSlaughterWeight;
    private EditText etSlaughterDate;
    private EditText etRfid;
    private EditText etMidSectionWeight;
    private EditText etCardCode;
    private Button btnSave;
    private Button btnCancel;
    private RadioGroup rgLeftRight;
    private RadioButton rbtnLeft;
    private RadioButton rbtnRight;

    // -----全局变量区-----------------------------------------------//

    /**
     * 转挂信息的数据模型，请不要直接操作视图，调用updateMidSectionInfo()方法会更新数据模型并刷新视图
     */
    private MidSectionInfoDTO globalMidSectionInfo = new MidSectionInfoDTO();

    private final ActivityHelper activityHelper = new ActivityHelper(this);



    /**
     * 消息队列数据接收器（消费者）
     */
    private final RabbitMqDataReceiver dataReceiver = new RabbitMqDataReceiver();


    /**
     * 接收旧芯片handler
     */
    ReceiveRfidIdHandler receiveOldRfidIdHandler = new ReceiveRfidIdHandler(this);
    private class ReceiveRfidIdHandler extends Handler {
        //防止内存泄露
        private final WeakReference<MidSectionActivity> activity;

        public ReceiveRfidIdHandler(MidSectionActivity activity) {
            this.activity = new WeakReference<MidSectionActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /**
             * DATE: 2022/8/25
             * mijiahao TODO: 根据芯片ID查询屠宰信息
             */
            String chipId = msg.obj.toString();
            queryMidSectionInfoByRfidId(chipId);
            globalMidSectionInfo.setSlaughterInfoRfid(chipId);
        }
    }



    /**
     * 转挂后接收芯片ID
     */
    MidSectionReceiveRfidIdHandler midSectionReceiveRfidIdHandler = new MidSectionReceiveRfidIdHandler(this);
    private class MidSectionReceiveRfidIdHandler extends Handler {
        //防止内存泄露
        private final WeakReference<MidSectionActivity> activity;

        public MidSectionReceiveRfidIdHandler(MidSectionActivity activity) {
            this.activity = new WeakReference<MidSectionActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            globalMidSectionInfo.setRfid(msg.obj.toString());
            updateMidSectionInfo(globalMidSectionInfo);
        }
    }


    // -----常量区-----------------------------------------------//
    /**
     * 通过芯片ID查询屠宰信息URL
     */
    private final String URL_GET_SLAUGHTER_INFO_BY_CHIP_ID = StringUrl.GetUrl()+"/api/mo/slaughterinfo/equal_find_with_inv";
    /**
     * 插入新记录URL
     */
    private final String URL_INSERT_MID_SECTION_INFO = StringUrl.GetUrl()+"/mo/midsectionInfo/save_or_update";

    private final String ACTIVITY_TITLE = "转挂";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mid_section);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataReceiver.closeConnection();
    }

    private void init(){
        //初始化UI组件
        initUiComponents();
        //接收旧芯片ID
        dataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_RFID_MID_SECTION_OLD_ONE, receiveOldRfidIdHandler);
        //接收新芯片ID
        dataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_RFID_MID_SECTION_NEW_ONE,midSectionReceiveRfidIdHandler);
        //添加按钮监听事件
        setBtnOnClickListener();
    }


    /**
     * 初始化ui组件
     */
    private void initUiComponents(){
        etBatchCode = findViewById(R.id.et_batch_code);
        etSlaughterDate = findViewById(R.id.et_slaughter_date);
        etSlaughterWeight = findViewById(R.id.et_slaughter_weight);
        etRfid = findViewById(R.id.et_rfid);
        etMidSectionWeight = findViewById(R.id.et_midSectionWeight);
        etCardCode = findViewById(R.id.et_card_code);
        titleView = findViewById(R.id.titleView);
        titleView.setAppTitle(ACTIVITY_TITLE);
        titleView.setLeftImgOnClickListener();
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        rgLeftRight = findViewById(R.id.rg_left_right);
        rbtnLeft = findViewById(R.id.rb_left);
        rbtnRight = findViewById(R.id.rb_right);
        globalMidSectionInfo.setInvRegion("L");
        //radioBtn监听事件
        rgLeftRight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_left){
                    globalMidSectionInfo.setInvRegion("L");
                    Log.e(this.getClass().getName(),"左！");
                }
                else if (checkedId == R.id.rb_right){
                    globalMidSectionInfo.setInvRegion("R");
                    Log.e(this.getClass().getName(),"右！");
                }
            }
        });
    }

    /**
     * 添加按钮监听事件
     */
    private void setBtnOnClickListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsRequiredCheck();
                Map<String,String> data = new HashMap<>();
                data.put("data",JSON.toJSONString(globalMidSectionInfo));
                OkHttpUtils.postAsyn(URL_INSERT_MID_SECTION_INFO,data,new HttpCallback(){
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        activityHelper.alertDialog(resultDesc.getmsg());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        Log.e(this.getClass().getName(),message);
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 更新屠宰信息的数据模型，并刷新视图
     *
     * @param midSectionInfoDTO 屠宰信息dto
     */
    private void updateMidSectionInfo(MidSectionInfoDTO midSectionInfoDTO) {
        try {
            if (midSectionInfoDTO == null) {
                activityHelper.alertDialog("更新数据模型不能为空");
                throw new Exception("更新数据模型不能为空");
            }
            globalMidSectionInfo = midSectionInfoDTO;
            refreshView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新视图
     */
    private void refreshView(){
        activityHelper.setText(etBatchCode,globalMidSectionInfo.getBatchCode());
        activityHelper.setText(etSlaughterWeight,globalMidSectionInfo.getMidsectionWeight());
        activityHelper.setText(etSlaughterDate,globalMidSectionInfo.getMidsectionDate());
        activityHelper.setText(etRfid,globalMidSectionInfo.getRfid());
    }

    /**
     * 参数空值检验
     *
     * @return boolean
     */
    boolean paramsRequiredCheck(){
        if (StringUtil.isEmpty(globalMidSectionInfo.getBatchCode())){
            activityHelper.alertDialog("批次号不能为空");
            return false;
        }
        if (StringUtil.isEmpty(globalMidSectionInfo.getRfid())){
            activityHelper.alertDialog("芯片ID不能为空");
            return false;
        }
        if (StringUtil.isEmpty(globalMidSectionInfo.getSlaughterInfoRfid())){
            activityHelper.alertDialog("起挂ID不能为空");
            return false;
        }
        return true;
    }

    /**
     * 根据芯片ID查询屠宰信息
     */
    public void queryMidSectionInfoByRfidId(String chipId){
        Map<String,String> queryMap = new HashMap<>();
        MidSectionInfo midSectionInfo = new MidSectionInfo();
        midSectionInfo.setRfid(chipId);
        queryMap.put("query", JSON.toJSONString(midSectionInfo));
        queryMap.put("page", "1");
        queryMap.put("limit", "1");
        OkHttpUtils.getAsyn(URL_GET_SLAUGHTER_INFO_BY_CHIP_ID,queryMap,new HttpCallback(){

            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                if (activityHelper.isResultDataEmpty(resultDesc)){
                    activityHelper.alertDialog("查询不到记录");
                    return;
                }
                List<SlaughterInfoDTO> slaughterInfoList = JSON.parseArray(resultDesc.getresult().toString(),SlaughterInfoDTO.class);
                //数据转换
                SlaughterInfoDTO slaughterInfo = slaughterInfoList.get(0);
                slaughterInfo.convertToMidSectionInfo(globalMidSectionInfo);
                updateMidSectionInfo(globalMidSectionInfo);
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Log.e(this.getClass().getName(),message);
            }
        });
    }




    }