package com.myapplication;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.Search.DepartmentSearch;
import com.myapplication.Search.WarehouseSearch;
import com.myapplication.dto.basicinfo.Warehouse;
import com.myapplication.dto.st.BarcodeProduct;
import com.myapplication.dto.st.RecordMainDTO;
import com.myapplication.utils.CommonAdapter;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.CustomStringUtils;
import com.myapplication.utils.DatePickDialogUtil;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.TitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialReturnActivity extends FragmentActivity {

    private TitleView titleView;
    private CommonAdapter commonAdapter;
    private ListView mListView;//列表
    private Button mbtnSave;//保存
    private Button mbtnCanel;//取消
    private Button mbtnDelete;//删行

    private List<BarcodeProduct> mDatas;  //明细列表数据

    private EditText etVouchDate;
    private EditText etCount;
    private EditText etOperateUser;
    private EditText etDepCode;
    private EditText etDepName;

    private CheckBox scanAdd;//
    private CheckBox scanDelete;//

    private EditText etWhCode;
    private EditText etWhName;

    private LoadingDailog dialog;

    private Handler handler;
    private int sRow=-1;
    private int IsSave=0;
    private int izEdit=0;
    private int scanState=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_return);
        //标题
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("材料退库单");
        titleView.setLeftImgOnClickListener();

        //主表控件获取
        etCount = (EditText) findViewById(R.id.count);
        etOperateUser = (EditText) findViewById(R.id.operateUser);
        etDepCode= (EditText) findViewById(R.id.depCode);
        etDepName= (EditText) findViewById(R.id.depName);
        etVouchDate= (EditText) findViewById(R.id.vouchDate);

        scanAdd= (CheckBox) findViewById(R.id.scanAdd);
        scanDelete= (CheckBox) findViewById(R.id.scanDelete);

        //设置默认扫描
        scanAdd.setChecked(true);

        scanAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanAdd.setChecked(true);
                scanDelete.setChecked(false);
                scanState=0;

            }
        });
        scanDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDelete.setChecked(true);
                scanAdd.setChecked(false);
                scanState=1;
            }
        });



        etWhCode= (EditText) findViewById(R.id.whCode);
        etWhName= (EditText) findViewById(R.id.whName);

        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                .setMessage("进行中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog=loadBuilder.create();


        mbtnSave=(Button)findViewById(R.id.btnSave);
        mbtnCanel=(Button)findViewById(R.id.btnCanel);
        mbtnDelete=(Button)findViewById(R.id.btnDelete);


//日期选择
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day =c.get(Calendar.DAY_OF_MONTH);
        etVouchDate.setText(year+"-"+month+"-"+day);
        etVouchDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickDialogUtil date=new DatePickDialogUtil(MaterialReturnActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if(year==1900)
                        {
                            etVouchDate.setText("");
                        }
                        else
                        {
                            etVouchDate.setText(year +"-" + (month + 1) + "-" + day);

                        }
                    }
                    //0,0指的是时间，true表示是否为24小时，true为24小时制

                });
                date.onCreateDialog().show();
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==99)
                {

                }



            }
        };

        //仓库选择
        etWhName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MaterialReturnActivity.this, WarehouseSearch.class);

                startActivityForResult(intent, 1);//
            }
        });

//仓库选择
        etDepName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MaterialReturnActivity.this, DepartmentSearch.class);
                startActivityForResult(intent, 1);//
            }
        });

        //条码列表获取
        mListView= (ListView) this.findViewById(R.id.list);

        //更新数据
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IsSave = 1;


                    if(mDatas==null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                        builder.setMessage("保存失败，数据不存在!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if(mDatas.size()==0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                        builder.setMessage("保存失败，数据不存在!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etWhCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                        builder.setMessage("保存失败，仓库不存在");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etDepCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                        builder.setMessage("保存失败，部门不存在");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    RecordMainDTO mainDTO=new RecordMainDTO();
                    mainDTO.setWhCode(etWhCode.getText().toString());
                    mainDTO.setWhName(etWhName.getText().toString());
                    mainDTO.setDepCode(etDepCode.getText().toString());
                    mainDTO.setDepName(etDepName.getText().toString());
                    mainDTO.setVouchDate(DateUntil.StrToDate(etVouchDate.getText().toString()));
//                    int nowRow=0;
//                    for(BarcodeProduct data :mDatas)
//                    {
//                        data.setWhCode(etWhCode.getText().toString());
//                        data.setWhName(etWhName.getText().toString());
//                        nowRow++;
//                    }
                    String strMain = JSON.toJSONString(mainDTO);
                    String strDetail = JSON.toJSONString(mDatas);
                    Map<String, String> mapquery = new HashMap<String, String>();
                    mapquery.put("mData", strMain );
                    mapquery.put("mDatas", strDetail );
                    mapquery.put("userName", StringUrl.GetUser() );
                    dialog.show();
                    OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/st/materialreturn/save", mapquery, new HttpCallback() {
                        @Override
                        public void onSuccess(ResultDesc resultDesc) {
                            super.onSuccess(resultDesc);
                            dialog.dismiss();

                            if (resultDesc.getsuccess() == true) {
                                mDatas=null;
                                initData();
                                InitDates(mDatas);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                                builder.setMessage("保存成功" );
                                builder.setPositiveButton("确定", null);
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                                builder.setMessage("保存出错，" + resultDesc.geterrormsg());
                                builder.setPositiveButton("确定", null);
                                builder.show();
                                IsSave = 0;
                            }
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            super.onFailure(code, message);
                            dialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                            builder.setMessage("保存出错，" + message);
                            builder.setPositiveButton("确定", null);
                            builder.show();
                            IsSave = 0;
                        }
                    });
                } catch (Exception e) {
                    dialog.dismiss();
                    IsSave = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
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
                    mDatas=null;
                    initData();
                    InitDates(mDatas);

                } catch (Exception e) {

                }

            }
        });

        //删行
        mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(sRow!=-1)
                    {
                        if(mDatas!=null)
                        {
                            BarcodeProduct q=mDatas.get(sRow);
                            if(q!=null)
                            {
                                initData();
                                mDatas.remove(q);
                                commonAdapter.notifyDataSetChanged();
                                if(mDatas!=null)
                                {
                                    etCount.setText(String.valueOf(mDatas.size()));
                                }
                                else
                                {
                                    etCount.setText("0");
                                }
                                sRow=-1;
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        });

        //初始化数据
        etOperateUser.setText(StringUrl.GetUser());

        //设置数据
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sRow=arg2;

            }


        });



        //扫描接口,注册
        IntentFilter S80BarCodeCheckFilter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        registerReceiver(m_S80BarCodeCheckReciever,S80BarCodeCheckFilter);

    }
    //扫描事件
    private BroadcastReceiver m_S80BarCodeCheckReciever = new  BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            String scanResult_1=intent.getStringExtra("SCAN_BARCODE1");
            String scanResult_2=intent.getStringExtra("SCAN_BARCODE2");
            Integer barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown
            String scanStatus=intent.getStringExtra("SCAN_STATE");
            if("ok".equals(scanStatus)){
                //成功
                BarScan(scanResult_1);
            }else{
                //失败如超时等
            }

        }
    };
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bundle bundle= data.getExtras();
        switch (resultCode) {

            //仓库列表数据返回
            case 1:
                String strWhCode= bundle.getString("whCode");
                String strWhName = bundle.getString("whName");

                if(CustomStringUtils.isNotBlank(strWhCode))
                {
                    //仓库和原来不同的时候清空货位
                    if(!strWhCode.equals("-1"))
                    {
                        etWhCode.setText(strWhCode);
                        etWhName.setText(strWhName);

                    }

                }


                break;

            default:
                break;
        }
    }


    //初始化条码列表数据
    private void  InitDates(List<BarcodeProduct> list)
    {

        try
        {
            commonAdapter = new CommonAdapter<BarcodeProduct>(this,list, R.layout.activity_material_return_item) {

                @Override
                protected void convertView(View item, BarcodeProduct s) {

                    if(s!=null)
                    {


                        if(s.getBarcode()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.barcode)).setText("卷号:"+s.getBarcode().toString());
                        }
                        else
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.barcode)).setText("卷号:");
                        }

                        if(s.getInvName()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("名称:"+s.getInvName().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("名称:");
                        }
                        if(s.getInvStd()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("规格:"+s.getInvStd().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("规格:");
                        }
                        if(s.getQty()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("数量:"+s.getQty().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("数量:");
                        }

                    }

                }


            };

            commonAdapter.notifyDataSetChanged();
            mListView.setAdapter(commonAdapter);

            if(mDatas!=null)
            {
                etCount.setText(String.valueOf(mDatas.size()));
            }
            else
            {
                etCount.setText("0");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void initData()
    {
        izEdit=1;

        izEdit=0;
    }

    private void BarScan(String barcode)
    {
        if(barcode.startsWith("bar-")){
            Map<String, String> mapquery = new HashMap<String, String>();
            mapquery.put("barcode", barcode.replace("bar-",""));
            OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/st/barcodeproduct/getbyid", mapquery, new HttpCallback() {
                @Override
                public void onSuccess(ResultDesc resultDesc) {
                    super.onSuccess(resultDesc);
                    try {
                        if (resultDesc.getsuccess()) {
                            if (resultDesc.getresult() != null && resultDesc.getresult() != "null") {
                                //获取后台返回的数据
                                BarcodeProduct data = JSON.parseObject(resultDesc.getresult().toString(), BarcodeProduct.class);
                                //主表数据解析
                                if (data != null) {
                                    if(data.getIzOut().equals("否"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                                        builder.setMessage("请扫描已出库条码");
                                        builder.setPositiveButton("确定", null);
                                        builder.show();
                                        return;
                                    }
                                    if (mDatas == null) {
                                        mDatas = new ArrayList<>();
                                    }


                                    int izExist=0;
                                    int nowRow=0;
                                    for (BarcodeProduct data1 : mDatas) {
                                        if (data1.getId().equals(data.getId())) {
                                            izExist=1;
                                            break;
                                        }

                                        nowRow++;
                                    }

                                    //0是新增，1是删除
                                    if(scanState==0)
                                    {
                                        if(izExist==0)
                                        {
                                            mDatas.add(data);
                                        }
                                        //默认仓库部门存在的带出
                                        if(CustomStringUtils.isNotBlank(data.getDefWhCode()))
                                        {
                                            etWhCode.setText(data.getDefWhCode());
                                            etWhName.setText(data.getDefWhName());
                                            etDepCode.setText(data.getDefDepCode());
                                            etDepName.setText(data.getDefDepName());
                                        }
                                    }
                                    else
                                    {
                                        if(izExist==1)
                                        {
                                            mDatas.remove(nowRow);
                                        }

                                    }

                                    //初始化主表数据
                                    InitDates(mDatas);
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                                    builder.setMessage("不符合的编码！" + resultDesc.geterrormsg());
                                    builder.setPositiveButton("确定", null);
                                    builder.show();
                                }
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                                builder.setMessage("不符合的编码！" + resultDesc.geterrormsg());
                                builder.setPositiveButton("确定", null);
                                builder.show();
                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialReturnActivity.this);
                            builder.setMessage("不符合的编码！" + resultDesc.geterrormsg());
                            builder.setPositiveButton("确定", null);
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



    }

}
