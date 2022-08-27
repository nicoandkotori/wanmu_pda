package com.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.myapplication.dto.mo.TetradInfoDTO;
import com.myapplication.utils.ActivityHelper;
import com.myapplication.utils.EnumUtils;
import com.myapplication.utils.RabbitMqDataReceiver;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TetradActivity extends FragmentActivity {


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
    private RadioGroup rgFrontRear;
    private RadioButton rbtnFront;
    private RadioButton rbtnRear;


    // -----全局变量区-----------------------------------------------//

    /**
     * 转挂信息的数据模型，请不要直接操作视图，调用updateMidSectionInfo()方法会更新数据模型并刷新视图
     */
    private TetradInfoDTO globalTetradInfoDTO = new TetradInfoDTO();

    private final ActivityHelper activityHelper = new ActivityHelper(this);

    private String midSectionRegion;

    /**
     * 二分体芯片接收handler
     */
    ReceiveMidSectionRfidIdHandler rfidIdHandler = new ReceiveMidSectionRfidIdHandler(this);
    private class ReceiveMidSectionRfidIdHandler extends Handler {
        //防止内存泄露
        private final WeakReference<TetradActivity> activity;

        public ReceiveMidSectionRfidIdHandler(TetradActivity activity) {
            this.activity = new WeakReference<TetradActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String chipId = msg.obj.toString();
            //获取二分体芯片ID
            globalTetradInfoDTO.setMidSectionInfoRfid(chipId);
            //查询二分体信息
            queryMidSectionInfoByRfidId();
        }
    }


    /**
     * 四分体芯片接收handler
     */
    ReceiveTetradRfidIdHandler receiveTetradRfidIdHandler = new ReceiveTetradRfidIdHandler(this);
    private class ReceiveTetradRfidIdHandler extends Handler {
        //防止内存泄露
        private final WeakReference<TetradActivity> activity;

        public ReceiveTetradRfidIdHandler(TetradActivity activity) {
            this.activity = new WeakReference<TetradActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String chipId = msg.obj.toString();
            globalTetradInfoDTO.setRfid(chipId);
            updateTetradInfo(globalTetradInfoDTO);
        }
    }

    /**
     * 消息队列数据接收器（消费者）
     */
    private final RabbitMqDataReceiver dataReceiver = new RabbitMqDataReceiver();
    /**
     * 是否选择了前的标志
     */
    private boolean isChooseFront = true;


    // -----常量区-----------------------------------------------//
    /**
     * 通过芯片ID查询二分体信息URL
     */
    private final String URL_GET_MIDSECTION_INFO_BY_CHIP_ID = StringUrl.GetUrl()+"/api/mo/midsectioninfo/equal_find";
    /**
     * 插入新记录URL
     */
    private final String URL_SAVE_TETRAD_INFO = StringUrl.GetUrl()+"/api/mo/tetradinfo/batch_save";

    private final String ACTIVITY_TITLE = "四分体分切";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetrad);
        init();
    }

    /**
     * -----必须关闭连接！-----
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataReceiver.closeConnection();
    }

    private void init(){
        initUiComponents();
        //接收旧芯片ID
        dataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_RFID_TETRAD_OLD_ONE,rfidIdHandler);
        //接收新芯片ID
        dataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_RFID_TETRAD_NEW_ONE,receiveTetradRfidIdHandler);
        setBtnOnclickListener();
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
        rgFrontRear = findViewById(R.id.rg_front_rear);
        rbtnFront = findViewById(R.id.rb_front);
        rbtnRear = findViewById(R.id.rb_rear);
        globalTetradInfoDTO.setInvRegion("F");
        //radioBtn监听事件
        rgFrontRear.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_front){
                    isChooseFront = true;

                }
                else if (checkedId == R.id.rb_rear){
                    isChooseFront = false;

                }
            }
        });
    }

    private void setBtnOnclickListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsRequiredCheck();
                String leftOrRightRegion = globalTetradInfoDTO.getInvRegion();
                //选择了“前”
                if (isChooseFront){
                    List<TetradInfoDTO> tetradInfoDTOS = new ArrayList<>();
                    try {
                        //生成两条数据去保存，前部分是被切下来的部分（牲畜是倒挂的，被切下来的部分是前半生），后部分是还挂着的部分
                        TetradInfoDTO front = globalTetradInfoDTO.clone();
                        TetradInfoDTO rear = globalTetradInfoDTO.clone();
                        front.setInvRegion(leftOrRightRegion+"F");
                        //还挂着的部分（后）取二分体芯片ID
                        rear.setRfid(globalTetradInfoDTO.getMidSectionInfoRfid());
                        rear.setInvRegion(leftOrRightRegion+"R");
                        tetradInfoDTOS.add(front);
                        tetradInfoDTOS.add(rear);
                        //设置操作人，操作时间
                        front.setOperationInfo();
                        rear.setOperationInfo();
                        batchSave(tetradInfoDTOS);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void batchSave(List<TetradInfoDTO> tetradInfoDTOList){
        Map<String,String> data = new HashMap<>();
        data.put("data",JSON.toJSONString(tetradInfoDTOList));
        OkHttpUtils.postAsyn(URL_SAVE_TETRAD_INFO,data,new HttpCallback(){
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                activityHelper.alertDialog(resultDesc.getmsg());
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                activityHelper.alertDialog(message);
            }
        });
    }

    /**
     * 更新屠宰信息的数据模型，并刷新视图
     *
     * @param tetradInfoDTO 四分体信息dto
     */
    private void updateTetradInfo(TetradInfoDTO tetradInfoDTO) {
        try {
            if (tetradInfoDTO == null) {
                activityHelper.alertDialog("更新数据模型不能为空");
                throw new Exception("更新数据模型不能为空");
            }
            globalTetradInfoDTO = tetradInfoDTO;
            refreshView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据芯片ID查询二分体信息
     */
    private void queryMidSectionInfoByRfidId(){
        Map<String,String> query = new HashMap<>();
        MidSectionInfo condition = new MidSectionInfo();
        condition.setRfid(globalTetradInfoDTO.getMidSectionInfoRfid());
        query.put("query", JSON.toJSONString(condition));
        query.put("page", "1");
        query.put("limit", "1");
        OkHttpUtils.getAsyn(URL_GET_MIDSECTION_INFO_BY_CHIP_ID,query,new HttpCallback(){

            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                if (activityHelper.isResultDataEmpty(resultDesc)){
                    activityHelper.alertDialog("查询不到该芯片ID绑定的二分体信息");
                    return;
                }
                List<MidSectionInfo> midSectionInfoList = JSON.parseArray(resultDesc.getresult().toString(),MidSectionInfo.class);
                MidSectionInfo midSectionInfo = midSectionInfoList.get(0);
                //数据转换赋值
                midSectionInfo.convertTetrad(globalTetradInfoDTO);
                updateTetradInfo(globalTetradInfoDTO);

            }
        });
    }

    /**
     * 刷新视图
     */
    private void refreshView(){
        activityHelper.setText(etBatchCode,globalTetradInfoDTO.getBatchCode());
        activityHelper.setText(etRfid,globalTetradInfoDTO.getRfid());
    }

    /**
     * 参数空值检验
     *
     * @return boolean
     */
    boolean paramsRequiredCheck(){
        if (StringUtil.isEmpty(globalTetradInfoDTO.getBatchCode())){
            activityHelper.alertDialog("批次号不能为空");
            return false;
        }
        if (StringUtil.isEmpty(globalTetradInfoDTO.getRfid())){
            activityHelper.alertDialog("芯片ID不能为空");
            return false;
        }
        if (StringUtil.isEmpty(globalTetradInfoDTO.getMidSectionInfoRfid())){
            activityHelper.alertDialog("二分体芯片ID不能为空");
            return false;
        }
        return true;
    }


}