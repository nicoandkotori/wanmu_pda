package com.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.dto.mo.SlaughterInfo;
import com.myapplication.utils.ActivityHelper;
import com.myapplication.utils.EnumUtils;
import com.myapplication.utils.RabbitMqDataReceiver;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.lang.ref.WeakReference;
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
    private Button btnUpdate;
    private Button btnCancel;



     // -----全局变量区-----------------------------------------------//

    /**
     * 起挂信息的数据模型，请不要直接操作视图，调用updateSlaughterInfo()方法会更新数据模型并刷新视图
     */
    private SlaughterInfo globalSlaughterInfo = new SlaughterInfo();
    /**
     * 芯片ID接收器
     */
    private final RabbitMqDataReceiver rabbitMqDataReceiver = new RabbitMqDataReceiver();
    /**
     * 芯片ID handler
     */
    private final RfidHandler rfidHandler = new RfidHandler(this);

    private class RfidHandler extends Handler {

        //防止内存泄露
        private final WeakReference<StartToHangActivity> activity;

        public RfidHandler(StartToHangActivity activity) {
            this.activity = new WeakReference<StartToHangActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            globalSlaughterInfo.setRfid(msg.obj.toString());
            updateSlaughterInfo(globalSlaughterInfo);
        }
    }
    private final ActivityHelper activityHelper = new ActivityHelper(this);


     // -----常量区-----------------------------------------------//

    private final String ACTIVITY_TITLE = "起挂";
    /**
     * url，查询一个批次中最先需要起挂的一条记录
     */
    private final String URL_GET_SLAUGHTER_INFO = StringUrl.GetUrl() + "/api/mo/slaughterinfo/getEarliestSlaughterInfo" ;
    /**
     * url，更新屠宰信息
     */
    private final String URL_UPDATE_SLAUGHTER_INFO = StringUrl.GetUrl() + "/api/mo/slaughterinfo/update" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_to_hang);
        init();

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
        //UI组件获取
        initUiComponents();
        //获取批次信息
        initBatchCode();
        //根据批次获得屠宰信息
        initSlaughterInfo();
        //接收芯片数据
        rabbitMqDataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_RFID_START_TO_HANG_OLD_ONE, rfidHandler);
        //添加按钮监听事件
        setButtonClickListener();
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
        btnUpdate = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    /**
     * 初始化批次信息
     */
    private void initBatchCode () {
        SlaughterInfo info = (SlaughterInfo) this.getIntent().getSerializableExtra("info");
        if (info == null) {
            return;
        }
        globalSlaughterInfo.setBatchCode(info.getBatchCode());
        updateSlaughterInfo(globalSlaughterInfo);
    }



    /**
     * 初始化起挂数据,查询一个批次中最先需要起挂的一条记录
     */
    private void initSlaughterInfo() {
        Map<String,String> queryMap = new HashMap<>(3);
        queryMap.put("batchCode", globalSlaughterInfo.getBatchCode());
        OkHttpUtils.postAsyn(URL_GET_SLAUGHTER_INFO,queryMap,new HttpCallback(){
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                if (resultDesc.getsuccess()){
                    String res=resultDesc.getresult().toString();
                    updateSlaughterInfo(JSON.parseObject(res, SlaughterInfo.class));;
                } else {
                    //查询不到当前批次的起挂数据
                    activityHelper.alertDialog(resultDesc.getmsg());
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                activityHelper.alertDialog(message);
            }
        });
    }

    /**
     * 设置按钮监听事件
     */
    private void setButtonClickListener(){
        //保存按钮事件
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> data = new HashMap<>(2);
                //设置过磅日期
                globalSlaughterInfo.setHookDate(new Date());
                data.put("data",JSON.toJSONString(globalSlaughterInfo));
                //参数空值校验
                if (!paramsRequiredCheck()){
                    return;
                }
                OkHttpUtils.postAsyn(URL_UPDATE_SLAUGHTER_INFO,data,new HttpCallback(){
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        activityHelper.alertDialog("保存成功！");
                        //获取当前批次的下一条记录里
                        initSlaughterInfo();
                        /**
                         * DATE: 2022/8/24
                         * mijiahao TODO: 如果下一条记录不存在要怎么处理
                         */
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        activityHelper.alertDialog(message);
                    }
                });
            }
        });
        //返回主菜单
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
     * @param slaughterInfo 屠宰信息
     */
    private void updateSlaughterInfo(SlaughterInfo slaughterInfo){
        try {
            if (slaughterInfo == null){
                activityHelper.alertDialog("更新数据模型不能为空");
                throw new Exception("更新数据模型不能为空");
            }
            globalSlaughterInfo = slaughterInfo;
            refreshView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 刷新视图
     */
    private void refreshView(){
       activityHelper.setText(etBatchCode,globalSlaughterInfo.getBatchCode());
       activityHelper.setText(etSlaughterWeight,globalSlaughterInfo.getSlaughterWeight());
       activityHelper.setText(etSlaughterDate,globalSlaughterInfo.getSlaughterDate());
       activityHelper.setText(etRfid,globalSlaughterInfo.getRfid());
    }

    /**
     * 参数空值检验
     *
     * @return boolean
     */
    boolean paramsRequiredCheck(){
        if (StringUtil.isEmpty(globalSlaughterInfo.getRfid())){
            activityHelper.alertDialog("芯片ID不能为空");
            return false;
        }
        return true;
    }


}