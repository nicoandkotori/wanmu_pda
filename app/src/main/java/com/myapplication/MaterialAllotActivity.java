package com.myapplication;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.Search.DepartmentSearch;
import com.myapplication.Search.WarehouseSearch;
import com.myapplication.dto.mo.ApplyOutDetail;
import com.myapplication.dto.st.AllotDetail;
import com.myapplication.dto.st.AllotMain;
import com.myapplication.dto.st.AppTransVouchs;
import com.myapplication.dto.st.BarcodeProduct;
import com.myapplication.dto.st.RecordMainDTO;
import com.myapplication.utils.CommonAdapter;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.CustomStringUtils;
import com.myapplication.utils.DatePickDialogUtil;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.DateUtil;
import com.myapplication.utils.DateWheelPickerDialog;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MaterialAllotActivity extends FragmentActivity {



    private TitleView titleView;
    private TabHost tabHost;//??????

    private CommonAdapter commonAdapter;
    private ListView mListView;//??????


    private CommonAdapter commonAdapterBarcode;
    private ListView mListViewBarcode;//???

    private Button mbtnSave;//??????
    private Button mbtnCanel;//??????
    private Button mbtnDelete;//??????
    private Button mbtnSelect;//???????????????
    private AllotMain mData;  //????????????

    private List<AppTransVouchs> mDatas;  //??????????????????
    private List<BarcodeProduct> mDataBarcode;  //??????????????????
    private EditText etOperateUser;
    private EditText etVouchDate;

    private EditText etOutWhCode;
    private EditText etOutWhName;
    private EditText etInWhCode;
    private EditText etInWhName;
    private EditText etOutDepCode;
    private EditText etOutDepName;

    private EditText etInDepCode;
    private EditText etInDepName;
    private EditText etVouchCode;
    private EditText etVouchId;
    private EditText etSourceType;


    private CheckBox scanAdd;//
    private CheckBox scanDelete;//

    private int scanState=0;
    private int sRow=-1;
    private int IsSave=0;
    private int izEdit=0;


    private int whState=0;
    private int depState=0;
    private Handler handler;
    private LoadingDailog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_allot);
        //??????
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("?????????");
        titleView.setLeftImgOnClickListener();


        //??????????????????

        etOperateUser = (EditText) findViewById(R.id.operateUser);
        etVouchDate = (EditText) findViewById(R.id.vouchDate);


        etOutWhCode = (EditText) findViewById(R.id.outWhCode);
        etOutWhName = (EditText) findViewById(R.id.outWhName);

        etInWhCode = (EditText) findViewById(R.id.inWhCode);
        etInWhName = (EditText) findViewById(R.id.inWhName);

        etOutDepCode = (EditText) findViewById(R.id.outDepCode);
        etOutDepName = (EditText) findViewById(R.id.outDepName);

        etInDepCode = (EditText) findViewById(R.id.inDepCode);
        etInDepName = (EditText) findViewById(R.id.inDepName);

        etVouchCode = (EditText) findViewById(R.id.vouchCode);

        etVouchId= (EditText) findViewById(R.id.vouchId);
        etVouchCode= (EditText) findViewById(R.id.vouchCode);
        etSourceType= (EditText) findViewById(R.id.sourceType);

        scanAdd= (CheckBox) findViewById(R.id.scanAdd);
        scanDelete= (CheckBox) findViewById(R.id.scanDelete);

        //??????????????????
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




        tabHost = (TabHost) findViewById(R.id.myTabHost);
        // ??????????????????TabActivity?????????????????????tabHost?????????????????????????????????tabHost.setup()
        tabHost.setup();


        // ??????content????????????????????????????????????view
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("????????????").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("????????????")
                .setContent(R.id.tab2));
        //???????????????
        tabHost.setOnTabChangedListener(new tabChangedListener());


        //?????????Tab??????????????????????????????
        updateTab(tabHost);
        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                .setMessage("?????????...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog=loadBuilder.create();


        mbtnSave=(Button)findViewById(R.id.btnSave);
        mbtnCanel=(Button)findViewById(R.id.btnCanel);
        mbtnDelete=(Button)findViewById(R.id.btnDelete);
        mbtnSelect=(Button)findViewById(R.id.btnSelect);


        //????????????
        etOutWhName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                whState=1;
//                Intent intent = new Intent(MaterialAllotActivity.this, WarehouseSearch.class);
//                startActivityForResult(intent, 1);//
            }
        });


        //????????????
        etInWhName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                whState=2;
//                Intent intent = new Intent(MaterialAllotActivity.this, WarehouseSearch.class);
//                startActivityForResult(intent, 1);//
            }
        });

        //????????????
        etOutDepName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                depState=1;
//                Intent intent = new Intent(MaterialAllotActivity.this, DepartmentSearch.class);
//                startActivityForResult(intent, 1);//
            }
        });

        //????????????
        etInDepName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                depState=2;
//                Intent intent = new Intent(MaterialAllotActivity.this, DepartmentSearch.class);
//                startActivityForResult(intent, 1);//
            }
        });
        //????????????
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day =c.get(Calendar.DAY_OF_MONTH);
        etVouchDate.setText(year+"-"+month+"-"+day);
        etVouchDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickDialogUtil date=new DatePickDialogUtil(MaterialAllotActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    //0,0??????????????????true???????????????24?????????true???24?????????

                });
                date.onCreateDialog().show();
            }
        });


        //??????????????????
        mListView= (ListView) this.findViewById(R.id.list);

        //??????????????????
        mListViewBarcode= (ListView) this.findViewById(R.id.listbarcode);



        //????????????
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IsSave = 1;

                    if(mDataBarcode==null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("??????????????????????????????!");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if(mDataBarcode.size()==0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("??????????????????????????????!");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etOutWhCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("????????????????????????????????????");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
//                    if (CustomStringUtils.isBlank(etOutDepCode.getText().toString())) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
//                        builder.setMessage("????????????????????????????????????");
//                        builder.setPositiveButton("??????",null);
//                        builder.show();
//                        IsSave=0;
//                        return;
//                    }
                    if (CustomStringUtils.isBlank(etInWhCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("????????????????????????????????????");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
//                    if (CustomStringUtils.isBlank(etInDepCode.getText().toString())) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
//                        builder.setMessage("????????????????????????????????????");
//                        builder.setPositiveButton("??????",null);
//                        builder.show();
//                        IsSave=0;
//                        return;
//                    }
                    if (CustomStringUtils.isBlank(etVouchId.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("?????????????????????????????????????????????");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etVouchDate.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                        builder.setMessage("??????????????????????????????");
                        builder.setPositiveButton("??????",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    AllotMain mainDTO=new AllotMain();
                    mainDTO.setVouchDate(DateUntil.StrToDate(etVouchDate.getText().toString()));
                    mainDTO.setInWhCode(etInWhCode.getText().toString());
                    mainDTO.setInWhName(etInWhName.getText().toString());
                    mainDTO.setOutWhCode(etOutWhCode.getText().toString());
                    mainDTO.setOutWhName(etOutWhName.getText().toString());
                    mainDTO.setInDepCode(etInDepCode.getText().toString());
                    mainDTO.setInDepName(etInDepName.getText().toString());

                    mainDTO.setOutDepCode(etOutDepCode.getText().toString());
                    mainDTO.setOutDepName(etOutDepName.getText().toString());
                    mainDTO.setSourceCode(etVouchCode.getText().toString());
                    mainDTO.setSourceId(etVouchId.getText().toString());
                    mainDTO.setSourceType(etSourceType.getText().toString());

                    String strMain = JSON.toJSONString(mainDTO);
                    String strDetail = JSON.toJSONString(mDataBarcode);
                    Map<String, String> mapquery = new HashMap<String, String>();
                    mapquery.put("mData", strMain );
                    mapquery.put("mDatas", strDetail );
                    mapquery.put("userName", StringUrl.GetUser() );
                    dialog.show();
                    OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/st/allot/save", mapquery, new HttpCallback() {
                        @Override
                        public void onSuccess(ResultDesc resultDesc) {
                            super.onSuccess(resultDesc);
                            dialog.dismiss();

                            if (resultDesc.getsuccess() == true) {
                                mDatas=null;
                                mDataBarcode=null;
                                initData();
                                InitDates(mDatas);
                                InitDateBarcode(mDataBarcode);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                builder.setMessage("????????????" );
                                builder.setPositiveButton("??????", null);
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                            builder.setMessage("???????????????" + message);
                            builder.setPositiveButton("??????", null);
                            builder.show();
                            IsSave = 0;
                        }
                    });
                } catch (Exception e) {
                    dialog.dismiss();
                    IsSave = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
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


                    mDatas=null;
                    mDataBarcode=null;
                    initData();
                    InitDates(mDatas);
                    InitDateBarcode(mDataBarcode);

                } catch (Exception e) {

                }

            }
        });

        //??????
        mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(sRow!=-1)
                    {
                        if(mDataBarcode!=null)
                        {
                            BarcodeProduct barcode=mDataBarcode.get(sRow);
                            if(barcode!=null)
                            {
                                mDataBarcode.remove(barcode);
                                commonAdapterBarcode.notifyDataSetChanged();



                                if(mDatas!=null)
                                {
                                    for(AppTransVouchs dispatchLists:mDatas)
                                    {
                                        if(dispatchLists.getCinvcode().equals(barcode.getInvCode()))
                                        {
                                            //????????????
                                            dispatchLists.setNowNum(dispatchLists.getNowNum().subtract(BigDecimal.ONE));
                                            InitDates(mDatas);
                                            break;
                                        }
                                    }

                                }



                                sRow=-1;
                            }
                        }
                    }

                } catch (Exception e) {

                }

            }
        });


        //?????????????????????
        mbtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getBaseContext(),AppTransSelectActivity.class);

                    startActivityForResult(intent, 1);//

                } catch (Exception e) {

                }

            }
        });

        //???????????????
        etOperateUser.setText(StringUrl.GetUser());

        //????????????
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }


        });

        //????????????
        mListViewBarcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sRow=arg2;
                if(sRow!=-1)
                {

                }
            }


        });


        //?????????????????????
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==99)
                {
                    InitDates(mDatas);
                    mDataBarcode=new ArrayList<>();
                    InitDateBarcode(mDataBarcode);
                }


            }
        };

        //????????????,??????
        IntentFilter S80BarCodeCheckFilter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        registerReceiver(m_S80BarCodeCheckReciever,S80BarCodeCheckFilter);
    }
    //????????????
    private BroadcastReceiver m_S80BarCodeCheckReciever = new  BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            String scanResult_1=intent.getStringExtra("SCAN_BARCODE1");
            String scanResult_2=intent.getStringExtra("SCAN_BARCODE2");
            Integer barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown
            String scanStatus=intent.getStringExtra("SCAN_STATE");
            if("ok".equals(scanStatus)){
                //??????
                BarScan(scanResult_1);
            }else{
                //??????????????????
            }

        }
    };
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bundle bundle= data.getExtras();
        switch (resultCode) {
            //????????????????????????
            case 1:
                String strWhCode= bundle.getString("whCode");
                String strWhName = bundle.getString("whName");

                if(CustomStringUtils.isNotBlank(strWhCode))
                {
                    if(!strWhCode.equals("-1"))
                    {
//                        if(whState==1)
//                        {
//                            etOutWhCode.setText(strWhCode);
//                            etOutWhName.setText(strWhName);
//                        }
//                        else
//                        {
//                            etInWhCode.setText(strWhCode);
//                            etInWhName.setText(strWhName);
//                        }


                    }

                }


                break;
            //????????????????????????
            case 2:
                String strDepCode= bundle.getString("depCode");
                String strDepName = bundle.getString("depName");

                if(CustomStringUtils.isNotBlank(strDepCode))
                {

                    if(!strDepCode.equals("-1"))
                    {
//                        if(whState==1)
//                        {
//
//                            etOutDepCode.setText(strDepCode);
//                            etOutDepName.setText(strDepName);
//                        }
//                        else
//                        {
//
//                            etInDepCode.setText(strDepCode);
//                            etInDepName.setText(strDepName);
//                        }


                    }

                }


                break;

            //?????????????????????????????????
            case 12:
                String strVouchId= bundle.getString("vouchId");
                String strVouchCode = bundle.getString("vouchCode");

                String strOutDepCode1 = bundle.getString("outDepCode");
                String strOutDepName1 = bundle.getString("outDepName");
                String strInDepCode1 = bundle.getString("inDepCode");
                String strInDepName1 = bundle.getString("inDepName");

                String strOutWhCode1 = bundle.getString("outWhCode");
                String strOutWhName1 = bundle.getString("outWhName");

                String strInWhCode1 = bundle.getString("inWhCode");
                String strInWhName1 = bundle.getString("inWhName");

                String strSourceType = bundle.getString("sourceType");
                if(CustomStringUtils.isNotBlank(strVouchId))
                {

                    if(!strVouchId.equals("-1"))
                    {
                        etVouchId.setText(strVouchId);
                        etVouchCode.setText(strVouchCode);

                        etOutDepCode.setText(strOutDepCode1);
                        etOutDepName.setText(strOutDepName1);
                        etInDepCode.setText(strInDepCode1);
                        etInDepName.setText(strInDepName1);

                        etInWhCode.setText(strInWhCode1);
                        etInWhName.setText(strInWhName1);


                        etOutWhCode.setText(strOutWhCode1);
                        etOutWhName.setText(strOutWhName1);


                        etSourceType.setText(strSourceType);


                        Map<String, String> mapquery = new HashMap<String, String>();
                        mapquery.put("id", strVouchId );
                        OkHttpUtils.postAsyn(StringUrl.GetUrl()+"/api/st/apptransvouch/getdetaillist",mapquery,new HttpCallback() {
                            @Override
                            public void onSuccess(ResultDesc resultDesc) {
                                super.onSuccess(resultDesc);
                                String res=resultDesc.getresult().toString();
                                mDatas= JSON.parseArray(res, AppTransVouchs.class);
                                Message message = Message.obtain();
                                message.arg1 =99 ;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                super.onFailure(code, message);

                            }
                        });


                    }
                    else
                    {
                        etVouchId.setText("");
                        etVouchCode.setText("");
                        etOutDepCode.setText("");
                        etOutDepName.setText("");
                        etInDepCode.setText("");
                        etInDepName.setText("");

                        etInWhCode.setText("");
                        etInWhName.setText("");


                        etOutWhCode.setText("");
                        etOutWhName.setText("");
                        etSourceType.setText("");
                        mDatas= new ArrayList<>();
                        Message message = Message.obtain();
                        message.arg1 =99 ;
                        handler.sendMessage(message);
                    }

                }


                break;
            default:
                break;
        }
    }


    //???????????????????????????
    private void  InitDates(List<AppTransVouchs> list)
    {
        try
        {


            commonAdapter = new CommonAdapter<AppTransVouchs>(this,list, R.layout.activity_material_allot_item) {

                @Override
                protected void convertView(View item, AppTransVouchs s) {

                    if(s!=null)
                    {


                        if(s.getInvName()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("??????:"+s.getInvName().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("??????:");
                        }
                        if(s.getInvStd()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("??????:"+s.getInvStd().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("??????:");
                        }
                        if(s.getItvnum()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.wqty)).setText("?????????:"+s.getItvnum().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.wqty)).setText("?????????:");
                        }
                        if(s.getNowNum()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("??????:"+s.getNowNum().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("??????:");
                        }

                    }

                }


            };

            commonAdapter.notifyDataSetChanged();
            mListView.setAdapter(commonAdapter);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    //???????????????????????????
    private void  InitDateBarcode(List<BarcodeProduct> list)
    {

        try
        {
            commonAdapterBarcode = new CommonAdapter<BarcodeProduct>(this,list, R.layout.activity_material_out_barcode_item) {

                @Override
                protected void convertView(View item, BarcodeProduct s) {

                    if(s!=null)
                    {


                        if(s.getBarcode()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.barcode)).setText("??????:"+s.getBarcode().toString());
                        }
                        else
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.barcode)).setText("??????:");
                        }

                        if(s.getInvName()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("??????:"+s.getInvName().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("??????:");
                        }
                        if(s.getInvStd()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("??????:"+s.getInvStd().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.invStd)).setText("??????:");
                        }
                        if(s.getQty()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("??????:"+s.getQty().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("??????:");
                        }
                        if(s.getPackCode()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.packCode)).setText("??????:"+s.getPackCode().toString());
                        }
                        else
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.packCode)).setText("??????:");
                        }
                    }

                }


            };

            commonAdapterBarcode.notifyDataSetChanged();
            mListViewBarcode.setAdapter(commonAdapterBarcode);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void initData()
    {
        izEdit=1;

        etVouchCode.setText("");
        etVouchId.setText("");
        etOutDepCode.setText("");
        etOutDepName.setText("");
        etInDepCode.setText("");
        etInDepName.setText("");

        etInWhCode.setText("");
        etInWhName.setText("");


        etOutWhCode.setText("");
        etOutWhName.setText("");
        etSourceType.setText("");
        izEdit=0;
    }

    private void BarScan(final String barcode)
    {
        //??????
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
                                //???????????????????????????
                                BarcodeProduct data = JSON.parseObject(resultDesc.getresult().toString(), BarcodeProduct.class);
                                //??????????????????
                                if (data != null) {
                                    //???????????????????????????????????????????????????
                                    if(data.getIzIn().equals("???"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                        builder.setMessage("??????????????????????????????");
                                        builder.setPositiveButton("??????", null);
                                        builder.show();
                                        return;
                                    }
                                    if(data.getIzOut().equals("???"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                        builder.setMessage("??????????????????????????????");
                                        builder.setPositiveButton("??????", null);
                                        builder.show();
                                        return;
                                    }

                                    if (mDataBarcode == null) {
                                        mDataBarcode = new ArrayList<>();
                                    }
                                    int izExist=0;
                                    int nowRow=0;
                                    for (BarcodeProduct data1 : mDataBarcode) {
                                        if(!data1.getWhCode().equals(data.getWhCode()))
                                        {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                            builder.setMessage("?????????????????????????????????");
                                            builder.setPositiveButton("??????", null);
                                            builder.show();
                                            return;
                                        }
                                        if (!data.getWhCode().equals(etOutWhCode.getText().toString())) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                            builder.setMessage("????????????????????????????????????");
                                            builder.setPositiveButton("??????", null);
                                            builder.show();
                                            return;
                                        }
                                        if (data1.getId().equals(data.getId())) {
                                            izExist=1;
                                            break;
                                        }


                                        nowRow++;
                                    }

                                    //0????????????1?????????
                                    if(scanState==0)
                                    {
                                        //???????????????????????????????????????????????????
                                        if(izExist==0)
                                        {
                                            if(mDatas!=null)
                                            {
                                                izExist=0;
                                                for(AppTransVouchs dispatchLists:mDatas)
                                                {
                                                    //?????????????????????????????????
                                                    if(dispatchLists.getCinvcode().equals(data.getInvCode()))
                                                    {
                                                        //??????+1
                                                        dispatchLists.setNowNum((dispatchLists.getNowNum()==null? BigDecimal.ZERO:dispatchLists.getNowNum()).add(BigDecimal.ONE));
                                                        InitDates(mDatas);

                                                        data.setRecordId(dispatchLists.getId().toString());
                                                        data.setRecordsId(dispatchLists.getAutoid().toString());
                                                        data.setRecordType(etSourceType.getText().toString());
                                                        mDataBarcode.add(data);
                                                        //?????????????????????
                                                        InitDateBarcode(mDataBarcode);
                                                        izExist=1;
                                                        break;
                                                    }
                                                }
                                                if(izExist==0)
                                                {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                    builder.setMessage("?????????????????????????????????");
                                                    builder.setPositiveButton("??????", null);
                                                    builder.show();
                                                }

                                            }
                                            else
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                builder.setMessage("??????????????????????????????");
                                                builder.setPositiveButton("??????", null);
                                                builder.show();
                                            }

                                        }



                                    }
                                    else
                                    {
                                        if(izExist==1)
                                        {
                                            mDataBarcode.remove(nowRow);

                                            for(AppTransVouchs dispatchLists:mDatas)
                                            {
                                                //?????????????????????????????????
                                                if(dispatchLists.getCinvcode().equals(data.getInvCode()))
                                                {
                                                    //??????-1
                                                    dispatchLists.setNowNum((dispatchLists.getNowNum()==null?BigDecimal.ZERO:dispatchLists.getNowNum()).subtract(BigDecimal.ONE));
                                                    InitDates(mDatas);
                                                    break;
                                                }
                                            }
                                        }

                                    }


                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                    builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                    builder.setPositiveButton("??????", null);
                                    builder.show();
                                }
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                builder.setPositiveButton("??????", null);
                                builder.show();
                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
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
        //??????
        else{
            if(barcode.startsWith("box-"))
            {
                Map<String, String> mapquery = new HashMap<String, String>();
                mapquery.put("barcode", barcode.replace("box-",""));
                OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/st/boxmain/getdetailbyid", mapquery, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        try {
                            if (resultDesc.getsuccess()) {
                                if (resultDesc.getresult() != null && resultDesc.getresult() != "null") {
                                    //???????????????????????????
                                    List<BarcodeProduct> list = JSON.parseArray(resultDesc.getresult().toString(), BarcodeProduct.class);
                                    if (list != null) {
                                        for(BarcodeProduct data:list) {

                                            if (data.getIzOut().equals("???")) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                builder.setMessage("??????????????????????????????");
                                                builder.setPositiveButton("??????", null);
                                                builder.show();
                                                return;
                                            }

                                            if (mDataBarcode == null) {
                                                mDataBarcode = new ArrayList<>();
                                            }
                                            for (BarcodeProduct data1 : mDataBarcode) {
                                                if(!data1.getWhCode().equals(data.getWhCode()))
                                                {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                    builder.setMessage("?????????????????????????????????");
                                                    builder.setPositiveButton("??????", null);
                                                    builder.show();
                                                    return;
                                                }

                                                if (!data.getWhCode().equals(etOutWhCode.getText().toString())) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                    builder.setMessage("????????????????????????????????????");
                                                    builder.setPositiveButton("??????", null);
                                                    builder.show();
                                                    return;
                                                }
                                            }


                                            if(mDatas!=null)
                                            {
                                                int izExist=0;
                                                for(AppTransVouchs dispatchLists:mDatas)
                                                {
                                                    //?????????????????????????????????
                                                    if(dispatchLists.getCinvcode().equals(data.getInvCode()))
                                                    {
                                                        izExist=1;
                                                        break;
                                                    }
                                                }
                                                if(izExist==0)
                                                {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                    builder.setMessage("??????:"+data.getBarcode()+"?????????????????????");
                                                    builder.setPositiveButton("??????", null);
                                                    builder.show();
                                                    return;
                                                }


                                            }
                                            else
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                                builder.setMessage("??????????????????????????????");
                                                builder.setPositiveButton("??????", null);
                                                builder.show();
                                                return;
                                            }
                                        }


                                        for(BarcodeProduct data:list)
                                        {
                                            int izExist=0;
                                            int nowRow=0;
                                            for (BarcodeProduct data1 : mDataBarcode) {
                                                if (data1.getId().equals(data.getId())) {
                                                    izExist=1;
                                                    break;
                                                }
                                                nowRow++;
                                            }

                                            //0????????????1?????????
                                            if(scanState==0)
                                            {
                                                //???????????????????????????????????????????????????
                                                if(izExist==0)
                                                {
                                                    for(AppTransVouchs dispatchLists:mDatas)
                                                    {
                                                        //?????????????????????????????????
                                                        if(dispatchLists.getCinvcode().equals(data.getInvCode()))
                                                        {
                                                            //??????+1
                                                            dispatchLists.setNowNum((dispatchLists.getNowNum()==null?BigDecimal.ZERO:dispatchLists.getNowNum()).add(BigDecimal.ONE));
                                                            InitDates(mDatas);


                                                            data.setRecordId(dispatchLists.getId().toString());
                                                            data.setRecordsId(dispatchLists.getAutoid().toString());
                                                            data.setRecordType(etSourceType.getText().toString());
                                                            mDataBarcode.add(data);
                                                            //?????????????????????
                                                            InitDateBarcode(mDataBarcode);
                                                            break;
                                                        }
                                                    }


                                                }




                                            }
                                            else
                                            {
                                                if(izExist==1)
                                                {
                                                    mDataBarcode.remove(nowRow);

                                                    for(AppTransVouchs dispatchLists:mDatas)
                                                    {
                                                        //?????????????????????????????????
                                                        if(dispatchLists.getCinvcode().equals(data.getInvCode()))
                                                        {
                                                            //??????-1
                                                            dispatchLists.setNowNum((dispatchLists.getNowNum()==null?BigDecimal.ZERO:dispatchLists.getNowNum()).subtract(BigDecimal.ONE));
                                                            InitDates(mDatas);
                                                            break;
                                                        }
                                                    }
                                                }

                                            }

                                        }




                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                        builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                        builder.setPositiveButton("??????", null);
                                        builder.show();
                                    }
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
                                    builder.setMessage("?????????????????????" + resultDesc.geterrormsg());
                                    builder.setPositiveButton("??????", null);
                                    builder.show();
                                }
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MaterialAllotActivity.this);
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
        }

    }




    /**
     * ??????Tab????????????????????????????????????
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost)
    {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            tv.setTypeface(Typeface.SERIF, 2); // ?????????????????????
            if (tabHost.getCurrentTab() == i)
            {
                //??????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//??????????????????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_p));
                view.setBackgroundResource(R.color.white);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
            else
            {
                //?????????
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//??????????????????
                view.setBackgroundResource(R.color.statusColor);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }

    /**
     * TabHost???????????????
     * @author
     *
     */
    private class tabChangedListener implements TabHost.OnTabChangeListener {

        @Override
        public void onTabChanged(String tabId)
        {
            tabHost.setCurrentTabByTag(tabId);
            updateTab(tabHost);
        }
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
