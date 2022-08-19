package com.myapplication;

import static android.R.id.list;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.tu.loadingdialog.LoadingDailog;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;

import com.myapplication.dto.st.DispatchList;
import com.myapplication.utils.CommonAdapter1;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.DatePickDialogUtil;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;
import com.myapplication.utils.TitleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vfun01 on 2017-11-08.
 */

public class DispatchSelectActivity extends FragmentActivity {

    private TitleView titleView;
    private ListView mListView;//列表
    private CommonAdapter1 commonAdapter;
    private ImageView mIvLeftImage;     // 左边的图片
    private Button mbtnSelect;//查询
    private Button mbtnYes;//查询
//    private Button mbtnAll;//全选

    private List<DispatchList> mDatas;  //列表数据

    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etCustName;
    private EditText etDispatchCode;
    private Handler handler;
    private int sRow=-1;
    private int IsSave=0;
    private int IsEdit=0;
    private int IsPress=0;

    private LoadingDailog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dispatch_select);

        Intent intent = getIntent();
        final String izReturn = intent.getStringExtra("izReturn");


        //标题
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("发货单查询");
        titleView.setLeftImgOnClickListener();
        //标题
        mIvLeftImage = (ImageView) findViewById(R.id.iv_left_image);
        mIvLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });

        //主表控件获取
        etStartDate = (EditText) findViewById(R.id.startDate);
        etCustName = (EditText) findViewById(R.id.custName);
        etEndDate= (EditText) findViewById(R.id.endDate);
        etDispatchCode = (EditText) findViewById(R.id.dispatchCode);
        //列表获取
        mListView = (ListView) this.findViewById(list);

        //日期选择
        Calendar c = Calendar.getInstance();
        int year =c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day =c.get(Calendar.DAY_OF_MONTH);
        etStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickDialogUtil date=new DatePickDialogUtil(DispatchSelectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if(year==1900)
                        {
                            etStartDate.setText("");
                        }
                        else
                        {
                            etStartDate.setText(year +"-" + (month + 1) + "-" + day);

                        }
                    }
                    //0,0指的是时间，true表示是否为24小时，true为24小时制

                });
                date.onCreateDialog().show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatePickDialogUtil date=new DatePickDialogUtil(DispatchSelectActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if(year==1900)
                        {
                            etEndDate.setText("");
                        }
                        else
                        {
                            etEndDate.setText(year +"-" + (month + 1) + "-" + day);

                        }
                    }
                    //0,0指的是时间，true表示是否为24小时，true为24小时制

                });
                date.onCreateDialog().show();
            }
        });



        mbtnSelect=(Button)findViewById(R.id.btnSelect);
        mbtnYes=(Button)findViewById(R.id.btnYes);
//        mbtnAll=(Button)findViewById(R.id.btnAll);

        //确认选中数据返回
        mbtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mDatas!=null&&sRow!=-1)
                {
                    DispatchList dispatchList=mDatas.get(sRow);
                    Intent intent=new Intent();
                    intent.putExtra("disId",dispatchList.getDlid().toString());
                    intent.putExtra("dispatchCode",dispatchList.getCdlcode());
                    intent.putExtra("custName",dispatchList.getCustName());
                    intent.putExtra("depName",dispatchList.getDepName());
                    intent.putExtra("depCode",dispatchList.getCdepcode());
                    setResult(10, intent);//回传数据到主Activity
                    finish(); //此方法后才能返回主Activity


                }


            }
        });
        //查询
        mbtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                //列表信息查询
                Map<String, String> mapquery = new HashMap<String, String>();
                DispatchList m=new DispatchList();
                m.setCdlcode(etDispatchCode.getText().toString());
                m.setCustName(etCustName.getText().toString());
                if(izReturn.equals("0"))
                {
                    m.setBreturnflag(false);
                }
                else
                {
                    m.setBreturnflag(true);
                }

                if(!StringUtil.isEmpty(etStartDate.getText().toString()) )
                {
                    java.util.Date iDate= DateUntil.StrToDate(etStartDate.getText().toString());
                    m.setVouchDateStart(iDate );
                }

                if(!StringUtil.isEmpty(etEndDate.getText().toString()) )
                {
                    java.util.Date iDate= DateUntil.StrToDate(etEndDate.getText().toString());
                    m.setVouchDateEnd(iDate );
                }
                String querydetail= JSON.toJSONString(m);
                mapquery.put("query", querydetail );
                OkHttpUtils.postAsyn(StringUrl.GetUrl()+"/api/st/dispatchlist/getlist",mapquery,new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        String res=resultDesc.getresult().toString();
                        mDatas= JSON.parseArray(res,DispatchList.class);
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
        });


        //仓库列表信息加载
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==99)
                {
                    InitDates(mDatas);
                }


            }
        };
        //设置数据

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                IsEdit=1;
                sRow=arg2;



                IsEdit=0;
                IsPress=0;
            }


        });



    }

    //初始化列表数据
    private void  InitDates(List<DispatchList> list)
    {

        try
        {
            commonAdapter = new CommonAdapter1<DispatchList>(this,list, R.layout.activity_dispatch_select_item) {

                @Override
                protected void convertView(final int pisition,View item, DispatchList s) {

                    if(s!=null)
                    {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        ((TextView) CommonViewHolder.get(item, R.id.dDate)).setText( "日期:"+ formatter.format(s.getDdate()));

                        if(s.getCdlcode()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.dispatchCode)).setText("单号:"+s.getCdlcode().toString());
                        }
                        else
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.dispatchCode)).setText("单号:");
                        }
                        if(s.getCustName()!=null)
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.custName)).setText("客户:"+s.getCustName().toString());
                        }
                        else
                        {
                            ((TextView) CommonViewHolder.get(item, R.id.custName)).setText("客户:");
                        }

//                        final CheckBox ck =  ((CheckBox) CommonViewHolder.get(item, R.id.izSelect));
//
//                        ck.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                SEOrderEntry arriveDetail=mDatas.get(pisition);
//                                if(arriveDetail!=null)
//                                {
//
//                                        //为true时设置为到货数量，否则0
//                                        if(ck.isChecked()==true)
//                                        {
//                                            arriveDetail.setIzSelect(true);
//
//                                        }
//                                        else
//                                        {
//                                            arriveDetail.setIzSelect(false);
//
//                                        }
//                                        commonAdapter.notifyDataSetChanged();
//                                    }
//
//
//
//                            }
//                        });

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
    @Override
    public void onBackPressed() {

        Close();

    }
    private void Close()
    {

        //关闭Activity
        Intent intent=new Intent();
        intent.putExtra("disId","-1");
        intent.putExtra("dispatchCode","");
        intent.putExtra("custName","");
        intent.putExtra("depName","");
        intent.putExtra("depCode","");
        setResult(10, intent);//回传数据到主Activity
        this.finish();
    }
}
