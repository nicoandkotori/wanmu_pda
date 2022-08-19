package com.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;

import com.myapplication.Search.DepartmentSearch;
import com.myapplication.Search.WarehouseSearch;
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
import com.myapplication.utils.TitleView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemiProductInActivity extends FragmentActivity   {


    private TabHost tabHost;//页签


    private TitleView titleView;
    private CommonAdapter commonAdapter;
    private ListView mListView;//列表


    private CommonAdapter commonAdapterBarcode;
    private ListView mListViewBarcode;//列表


    private Button mbtnSave;//保存
    private Button mbtnCanel;//取消
    private Button mbtnDelete;//删行

//    private List<BarcodeProduct> mDatas;  //明细列表数据
    private List<BarcodeProduct> mDataBarcode;  //条码列表数据

    private EditText etVouchDate;
    private EditText etOperateUser;

    private EditText etWhCode;
    private EditText etWhName;
    private EditText etDepCode;
    private EditText etDepName;

    private CheckBox scanAdd;//
    private CheckBox scanDelete;//

    private Dialog dateDialog;
    private LoadingDailog dialog;

    private Handler handler;
    private int sRow=-1;
    private int IsSave=0;
    private int izEdit=0;


    private int scanState=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semi_product_in);
        //标题
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("半成品入库");
        titleView.setLeftImgOnClickListener();

        //主表控件获取

        etOperateUser = (EditText) findViewById(R.id.operateUser);
        etVouchDate= (EditText) findViewById(R.id.vouchDate);
        etDepCode= (EditText) findViewById(R.id.depCode);
        etDepName= (EditText) findViewById(R.id.depName);

        etWhCode= (EditText) findViewById(R.id.whCode);
        etWhName= (EditText) findViewById(R.id.whName);
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




        tabHost = (TabHost) findViewById(R.id.myTabHost);
        // 如果不是继承TabActivity，则必须在得到tabHost之后，添加标签之前调用tabHost.setup()
        tabHost.setup();


        // 这里content的设置采用了布局文件中的view
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("基本信息").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("条码信息")
                .setContent(R.id.tab2));
        //选择监听器
        tabHost.setOnTabChangedListener(new tabChangedListener());


        //初始化Tab的颜色，和字体的颜色
        updateTab(tabHost);
        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                .setMessage("进行中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog=loadBuilder.create();


        mbtnSave=(Button)findViewById(R.id.btnSave);
        mbtnCanel=(Button)findViewById(R.id.btnCanel);
        mbtnDelete=(Button)findViewById(R.id.btnDelete);



        //仓库选择
        etWhName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(SemiProductInActivity.this, WarehouseSearch.class);
                intent.putExtra("izProduct","0");
                startActivityForResult(intent, 1);//
            }
        });

        //仓库选择
        etDepName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(SemiProductInActivity.this, DepartmentSearch.class);
                startActivityForResult(intent, 1);//
            }
        });
//        etVouchDate.setText(DateUtil.getToday());
//        etVouchDate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                showDateDialog(etVouchDate,DateUtil.getDateForString(etVouchDate.getText().toString()));
//            }
//        });
        //日期选择
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day =c.get(Calendar.DAY_OF_MONTH);
        etVouchDate.setText(year+"-"+month+"-"+day);
        etVouchDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickDialogUtil date=new DatePickDialogUtil(SemiProductInActivity.this, new DatePickerDialog.OnDateSetListener() {
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


        //条码列表获取
        mListView= (ListView) this.findViewById(R.id.list);

        //条码列表获取
        mListViewBarcode= (ListView) this.findViewById(R.id.listbarcode);



        //更新数据
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IsSave = 1;

                    if(mDataBarcode==null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                        builder.setMessage("保存失败，数据不存在!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if(mDataBarcode.size()==0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                        builder.setMessage("保存失败，数据不存在!");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etWhCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                        builder.setMessage("保存失败，仓库不存在");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etDepCode.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                        builder.setMessage("保存失败，部门不存在");
                        builder.setPositiveButton("确定",null);
                        builder.show();
                        IsSave=0;
                        return;
                    }
                    if (CustomStringUtils.isBlank(etVouchDate.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                        builder.setMessage("保存失败，日期不存在");
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
                    String strDetail = JSON.toJSONString(mDataBarcode);
                    Map<String, String> mapquery = new HashMap<String, String>();
                    mapquery.put("mData", strMain );
                    mapquery.put("mDatas", strDetail );
                    mapquery.put("userName", StringUrl.GetUser() );
                    dialog.show();
                    OkHttpUtils.postAsyn(StringUrl.GetUrl() + "/api/st/semiproductin/save", mapquery, new HttpCallback() {
                        @Override
                        public void onSuccess(ResultDesc resultDesc) {
                            super.onSuccess(resultDesc);
                            dialog.dismiss();

                            if (resultDesc.getsuccess() == true) {
//                                mDatas=null;
                                mDataBarcode=null;
                                initData();
                                InitDates(mDataBarcode);
                                InitDateBarcode(mDataBarcode);
                                AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                                builder.setMessage("保存成功" );
                                builder.setPositiveButton("确定", null);
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                            builder.setMessage("保存出错，" + message);
                            builder.setPositiveButton("确定", null);
                            builder.show();
                            IsSave = 0;
                        }
                    });
                } catch (Exception e) {
                    dialog.dismiss();
                    IsSave = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
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


//                    mDatas=null;
                    mDataBarcode=null;
                    initData();
                    InitDates(mDataBarcode);
                    InitDateBarcode(mDataBarcode);

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
                        if(mDataBarcode!=null)
                        {
                            BarcodeProduct barcode=mDataBarcode.get(sRow);
                            if(barcode!=null)
                            {
                                mDataBarcode.remove(barcode);
                                commonAdapterBarcode.notifyDataSetChanged();


                                //加载汇总数据
                                InitDates(mDataBarcode);


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

            }


        });

        //设置数据
        mListViewBarcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sRow=arg2;
                if(sRow!=-1)
                {

                }
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
                    if(!strWhCode.equals("-1"))
                    {
                        etWhCode.setText(strWhCode);
                        etWhName.setText(strWhName);

                    }

                }


                break;
            //部门列表数据返回
            case 2:
                String strDepCode= bundle.getString("depCode");
                String strDepName = bundle.getString("depName");

                if(CustomStringUtils.isNotBlank(strDepCode))
                {

                    if(!strDepCode.equals("-1"))
                    {
                        etDepCode.setText(strDepCode);
                        etDepName.setText(strDepName);

                    }

                }


                break;
            default:
                break;
        }
    }


    //初始化条码列表数据
    private void  InitDates(List<BarcodeProduct> list1)
    {
        try
        {
            List<BarcodeProduct> listData=new ArrayList<>();
            if(list1!=null)
            {
                for (BarcodeProduct m:list1)
                {
                    int izExist=0;
                    for(BarcodeProduct q:listData)
                    {
                        if(m.getInvCode().equals(q.getInvCode()))
                        {
                            izExist=1;
                            q.setCount(q.getCount()+1);
                            q.setQty(q.getQty().add(m.getQty()));
                        }
                    }

                    if(izExist==0)
                    {
                        BarcodeProduct barcodeProduct=new BarcodeProduct();
                        barcodeProduct.setInvCode(m.getInvCode());
                        barcodeProduct.setInvName(m.getInvName());
                        barcodeProduct.setInvStd(m.getInvStd());
                        barcodeProduct.setCount(1);
                        barcodeProduct.setQty(m.getQty());
                        listData.add(barcodeProduct);
                    }
                }

            }

            commonAdapter = new CommonAdapter<BarcodeProduct>(this,listData, R.layout.activity_semi_product_in_item) {

                @Override
                protected void convertView(View item, BarcodeProduct s) {

                    if(s!=null)
                    {


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
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("米数:"+s.getQty().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("米数:");
                        }
                        if(s.getCount()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.count)).setText("数量:"+s.getCount().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.count)).setText("数量:");
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


    //初始化条码列表数据
    private void  InitDateBarcode(List<BarcodeProduct> list)
    {

        try
        {
            commonAdapterBarcode = new CommonAdapter<BarcodeProduct>(this,list, R.layout.activity_semi_product_in_barcode_item) {

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
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("米数:"+s.getQty().toString());
                        }
                        else {
                            ((TextView) CommonViewHolder.get(item, R.id.qty)).setText("米数:");
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

        etDepCode.setText("");
        etDepName.setText("");
        etWhName.setText("");
        etWhCode.setText("");

        izEdit=0;
    }

    private void BarScan(final String barcode)
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
                                    if(data.getIzProduct()==1)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                                        builder.setMessage("请扫描半成品条码");
                                        builder.setPositiveButton("确定", null);
                                        builder.show();
                                        return;
                                    }

                                    if (mDataBarcode == null) {
                                        mDataBarcode = new ArrayList<>();
                                    }
                                    int izExist=0;
                                    int nowRow=0;
                                    for (BarcodeProduct data1 : mDataBarcode) {
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
                                            mDataBarcode.add(data);
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
                                            mDataBarcode.remove(nowRow);
                                        }

                                    }



                                    //初始化主表数据
                                    InitDateBarcode(mDataBarcode);
                                    //初始化主表数据
                                    InitDates(mDataBarcode);




                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                                    builder.setMessage("不符合的编码！" + resultDesc.geterrormsg());
                                    builder.setPositiveButton("确定", null);
                                    builder.show();
                                }
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
                                builder.setMessage("不符合的编码！" + resultDesc.geterrormsg());
                                builder.setPositiveButton("确定", null);
                                builder.show();
                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(SemiProductInActivity.this);
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




    /**
     * 更新Tab标签的颜色，和字体的颜色
     * @param tabHost
     */
    private void updateTab(final TabHost tabHost)
    {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++)
        {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
            if (tabHost.getCurrentTab() == i)
            {
                //选中
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//选中后的背景
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_p));
                view.setBackgroundResource(R.color.white);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
            else
            {
                //不选中
//                view.setBackground(getResources().getDrawable(R.drawable.back_close_n));//非选择的背景
                view.setBackgroundResource(R.color.statusColor);
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            }
        }
    }

    /**
     * TabHost选择监听器
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

    private void showDateDialog(EditText editText,List<Integer> date) {
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
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
