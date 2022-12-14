package com.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.device.ScanManager;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.dto.mo.SlaughterInfo;
import com.myapplication.utils.CommonAdapter;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.CustomStringUtils;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.DateUtil;
import com.myapplication.utils.DateWheelPickerDialog;
import com.myapplication.utils.EnumUtils;
import com.myapplication.utils.RabbitMqDataReceiver;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.TitleView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeighQueueActivity extends FragmentActivity {
    private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
    public static Activity mActivity;
    private ActionBar actionBar;
    private EditText showScanResult;
    private Button btn;
    private Button mScan;
    private Button mClose;
    private int type;
    private int outPut;

    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;


    private TabHost tabHost;//??????


    private TitleView titleView;
    private CommonAdapter commonAdapter;
    private ListView mListView;//??????
    private TextView tvWeightNumber;

    private CommonAdapter commonAdapterBarcode;

    private Button mbtnSave;//??????
    private Button mbtnCanel;//??????
    private Button mbtnSelect1;//??????
    private List<SlaughterInfo> mDatas;  //??????????????????


    //??????????????????
    private EditText etModId;// = (EditText) findViewById(R.id.modId);
    private EditText etbatchCode;// = (EditText) findViewById(R.id.batchCode);
    private EditText etVouchCode;
    private EditText etVouchDate;
    private EditText etInvName;
    private EditText etPlanQty;

    private Dialog dateDialog;
    private LoadingDailog dialog;

    private Handler handler;
    private int sRow = -1;
    private int IsSave = 0;
    private int izEdit = 0;
    /**
     * rabbitmq???????????????
     */
    private RabbitMqDataReceiver rabbitMqDataReceiver = new RabbitMqDataReceiver();


    //??????????????? ReceiveWeightHandler?????????????????????????????????
    private class ReceiveWeightHandler extends Handler {

        //??????????????????
        private final WeakReference<WeighQueueActivity> activity;

        public ReceiveWeightHandler(WeighQueueActivity activity) {
            this.activity = new WeakReference<WeighQueueActivity>(activity) ;
        }

        @Override
        public void handleMessage(Message msg) {
            tvWeightNumber.setText(msg.obj.toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        if (intent.getBooleanExtra("destroyFlag", false)) {
            finish();
            return;
        }
        setContentView(R.layout.activity_weigh_queue);
        //??????
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("????????????");
        titleView.setLeftImgOnClickListener();
        etModId = (EditText) findViewById(R.id.modId);
        etbatchCode = (EditText) findViewById(R.id.batchCode);

        etVouchCode = (EditText) findViewById(R.id.vouchCode);
        etVouchDate = (EditText) findViewById(R.id.vouchDate);
        etInvName = (EditText) findViewById(R.id.invName);
        etPlanQty = (EditText) findViewById(R.id.planQty);
        tvWeightNumber = (TextView) findViewById(R.id.textView_number_weight);
        //???????????????
        setData();


        tabHost = (TabHost) findViewById(R.id.myTabHost);
        // ??????????????????TabActivity?????????????????????tabHost?????????????????????????????????tabHost.setup()
        tabHost.setup();


        // ??????content????????????????????????????????????view
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("??????????????????").setContent(R.id.tab1));
        /*tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("????????????")
                .setContent(R.id.tab2));*/
        //???????????????
        tabHost.setOnTabChangedListener(new tabChangedListener());
        //?????????Tab??????????????????????????????
        updateTab(tabHost);
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage("?????????...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        mbtnSave = (Button) findViewById(R.id.btnSave);
        mbtnCanel = (Button) findViewById(R.id.btnCanel);
        mbtnSelect1 = (Button) findViewById(R.id.btnSelect1);

        //??????????????????
        mListView = (ListView) this.findViewById(R.id.list);
        //??????????????????
        ReceiveWeightHandler receiveWeightHandler = new ReceiveWeightHandler(this);
        rabbitMqDataReceiver.beginConsume(EnumUtils.RABBIT_MQ_QUEUE.QUEUE_WEIGHT_ONE, receiveWeightHandler);
        //????????????
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IsSave = 1;


                    if (mDatas == null || mDatas.size() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                        builder.setMessage("?????????????????????");
                        builder.setPositiveButton("??????", null);
                        builder.show();
                        return;
                    }
                    SlaughterInfo slaughterInfo = mDatas.get(0);
                    slaughterInfo.setSlaughterWeight(new BigDecimal(tvWeightNumber.getText().toString()));
                    slaughterInfo.setSlaughterDate(new Date());
                    slaughterInfo.setSlaughterOperName(StringUrl.GetUser());
                    mDatas.set(0,slaughterInfo);
                    for (SlaughterInfo entity : mDatas) {
                        entity.setMoDetailId(etModId.getText().toString());
                        entity.setBatchCode(etbatchCode.getText().toString());
                    }
                    String strDetail = JSON.toJSONString(mDatas);
                    Map<String, String> mapquery = new HashMap<String, String>();
                    mapquery.put("mDatas", strDetail);
                    mapquery.put("userName", StringUrl.GetUser());
                    dialog.show();
                    OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/mo/slaughterinfo/liveCattleWeighSave", mapquery, new HttpCallback() {
                        @Override
                        public void onSuccess(ResultDesc resultDesc) {
                            super.onSuccess(resultDesc);
                            dialog.dismiss();
                            if (resultDesc.getsuccess() == true) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                                builder.setMessage("????????????");
                                builder.setPositiveButton("??????", null);
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                                builder.setMessage("???????????????" + resultDesc.geterrormsg());
                                builder.setPositiveButton("??????", null);
                                builder.show();
                                IsSave = 0;
                            }
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                            builder.setMessage("???????????????" + message);
                            builder.setPositiveButton("??????", null);
                            builder.show();
                            IsSave = 0;
                        }
                    });
                } catch (Exception e) {
                    dialog.dismiss();
                    IsSave = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("??????", null);
                    builder.show();
                }
            }
        });

        mbtnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    WeighQueueActivity.this.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        //????????????
        mbtnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BarScan(((EditText) findViewById(R.id.barcode)).getText().toString());
                } catch (Exception e) {

                }

            }
        });

        //????????????
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sRow = arg2;
                if (sRow != -1) {

                }
            }


        });


        //?????????????????????
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == 99) {
                    mDatas = new ArrayList<>();
                    InitDates(mDatas);
                }
            }
        };

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bundle bundle = data.getExtras();
        switch (resultCode) {
            case 10:
                String strMoidId = bundle.getString("modId");
                String strBatchCode = bundle.getString("batchCode");
                String strVouchCode = bundle.getString("vouchCode");
                String strVouchDate = bundle.getString("vouchDate");
                String strInvName = bundle.getString("invName");
                String strPlanQty = bundle.getString("planQty");
                if (CustomStringUtils.isNotBlank(strMoidId)) {

                    if (!strMoidId.equals("-1")) {
                        etModId.setText(strMoidId);
                        etbatchCode.setText(strBatchCode);
                        etVouchCode.setText(strVouchCode);
                        etVouchDate.setText(strVouchDate);
                        etInvName.setText(strInvName);
                        etPlanQty.setText(strPlanQty);
                    } else {
                        etModId.setText("");
                        etbatchCode.setText("");
                        etVouchCode.setText("");
                        etVouchDate.setText("");
                        etInvName.setText("");
                        etPlanQty.setText("");
                    }
                }

                break;
            default:
                break;
        }
    }


    //???????????????????????????
    private void InitDates(List<SlaughterInfo> list) {
        try {
            commonAdapter = new CommonAdapter<SlaughterInfo>(this, list, R.layout.activity_weigh_queue_item) {

                @Override
                protected void convertView(View item, SlaughterInfo s) {

                    if (s != null) {


                        if (s.getCardCode() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.cardCode)).setText("??????:" + s.getCardCode().toString());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.cardCode)).setText("??????:");
                        }
                        if (s.getVenName() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.venName)).setText("?????????:" + s.getVenName().toString());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.venName)).setText("?????????:");
                        }

                        if (s.getSlaughterWeight() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.weight)).setText("??????:" + s.getSlaughterWeight().toString());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.weight)).setText("??????:");
                        }
                    }

                }


            };

            commonAdapter.notifyDataSetChanged();
            mListView.setAdapter(commonAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * ????????????????????????????????????????????????
     */
    private void setData() {
        SlaughterInfo info = (SlaughterInfo) this.getIntent().getSerializableExtra("info");
        if (info == null) {
            return;
        }
        etModId.setText(info.getMoDetailId());
        etbatchCode.setText(info.getBatchCode());
        etVouchCode.setText(info.getVouchCode());
        etVouchDate.setText(DateUntil.getChineseDate(info.getVouchDate()));
        etInvName.setText(info.getInvName());
        etPlanQty.setText(String.format(info.getPlanQty().toString()));
        mDatas = new ArrayList<SlaughterInfo>();
        mDatas.add(info);
        izEdit = 0;
    }


    private void initData() {
        izEdit = 1;

        etModId.setText("");
        etbatchCode.setText("");
        etVouchCode.setText("");
        etVouchDate.setText("");
        etInvName.setText("");
        etPlanQty.setText("");
        izEdit = 0;
    }


    private void BarScan(final String barcode) {
        //??????
        Map<String, String> mapquery = new HashMap<String, String>();
        mapquery.put("barcode", barcode);
        OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/mo/cardInfo/getByCardCode", mapquery, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    if (resultDesc.getsuccess()) {
                        if (resultDesc.getresult() != null && resultDesc.getresult() != "null") {
                            //???????????????????????????
                            SlaughterInfo data = JSON.parseObject(resultDesc.getresult().toString(), SlaughterInfo.class);
                            //??????????????????
                            if (data != null) {

                                if (mDatas == null) {
                                    mDatas = new ArrayList<>();
                                }

                                for (SlaughterInfo entity : mDatas) {
                                    if (entity.getCardCode().equals(data.getCardCode())) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                                        builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                        builder.setPositiveButton("??????", null);
                                        builder.show();
                                        return;
                                    }
                                }

                                data.setCardId(data.getId());
                                data.setId("");
                                data.setSlaughterWeight(BigDecimal.ONE);
                                mDatas.add(data);
                                InitDates(mDatas);

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                                builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                builder.setPositiveButton("??????", null);
                                builder.show();
                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                            builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                            builder.setPositiveButton("??????", null);
                            builder.show();
                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(WeighQueueActivity.this);
                        builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                        builder.setPositiveButton("??????", null);
                        builder.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);

            }
        });
    }


    /**
     * ??????Tab????????????????????????????????????
     *
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            tv.setTypeface(Typeface.SERIF, 2); // ?????????????????????
            if (tabHost.getCurrentTab() == i) {
                //??????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//??????????????????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_p));
                view.setBackgroundResource(R.color.white);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            } else {
                //?????????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//??????????????????
                view.setBackgroundResource(R.color.statusColor);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }

    /**
     * TabHost???????????????
     *
     * @author
     */
    private class tabChangedListener implements TabHost.OnTabChangeListener {

        @Override
        public void onTabChanged(String tabId) {
            tabHost.setCurrentTabByTag(tabId);
            updateTab(tabHost);
        }
    }

    @Override
    protected void onDestroy() {
        if (rabbitMqDataReceiver != null) {
            rabbitMqDataReceiver.closeConnection();
        }
        super.onDestroy();
    }

    private void showDateDialog(EditText editText, List<Integer> date) {
        final EditText mEditText = editText;
        DateWheelPickerDialog.Builder builder = new DateWheelPickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DateWheelPickerDialog.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int[] dates) {
                        mEditText.setText(String.format("%d-%s-%s", dates[0], dates[1] > 9 ? dates[1] : ("0" + dates[1]), dates[2] > 9 ? dates[2] : ("0" + dates[2])));
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);
        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    /**
     * ????????????
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


}
