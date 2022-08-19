package com.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapplication.dto.mo.MoOrderInfo;
import com.myapplication.dto.mo.SlaughterInfo;
import com.myapplication.utils.CommonAdapter1;
import com.myapplication.utils.CommonViewHolder;
import com.myapplication.utils.DateUntil;
import com.myapplication.utils.TitleView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SearchMoOrderActivity extends FragmentActivity {

    private TextView dt;
    private CommonAdapter1 commonAdapter;
    private TitleView titleView;
    private ListView mListView;//列表
    private ImageView mIvLeftImage;     // 左边的图片
    private List<MoOrderInfo> mDatas;  //列表数据
    private Handler handler;
    private Button confirmButton;
    private Button cancelButton;
    private int sRow = -1;
    private int IsSave = 0;
    private int IsEdit = 0;
    private int IsPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_mo_order);

        //标题
        titleView = (TitleView) findViewById(R.id.titleView);
        titleView.setAppTitle("批次查询");
        titleView.setLeftImgOnClickListener();

        confirmButton = (Button) findViewById(R.id.btnConfirm);
        cancelButton = (Button) findViewById(R.id.btnCanel);
        //列表获取
        mListView = (ListView) this.findViewById(R.id.search_order_list);
        dt = findViewById(R.id.date);
        //设置当前日期
        dt.setText(DateUntil.getChineseDate(new Date()));
        //初始化列表数据
        Intent intent = this.getIntent();
        mDatas = (List<MoOrderInfo>) intent.getSerializableExtra("info");
        initDataList(mDatas);
        //设置数据
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                IsEdit = 1;
                sRow = arg2;
                IsEdit = 0;
                IsPress = 0;
            }
        });
        //点击确定按钮跳转
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoOrderInfo orderInfo = mDatas.get(sRow);
                Class target = (Class) SearchMoOrderActivity.this.getIntent().getSerializableExtra("targetActivity");
                Intent intent = new Intent(SearchMoOrderActivity.this,target);
                SlaughterInfo slaughterInfo = new SlaughterInfo();
                slaughterInfo.transformFromMoOrderInfo(orderInfo);
                intent.putExtra("info",slaughterInfo);
                startActivity(intent);
                SearchMoOrderActivity.this.finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    //初始化列表数据
    private void initDataList(List<MoOrderInfo> list) {

        try {
            commonAdapter = new CommonAdapter1<MoOrderInfo>(this, list, R.layout.search_mo_order_item) {

                @Override
                protected void convertView(final int pisition, View item, MoOrderInfo s) {

                    if (s != null) {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


                        if (s.getVouchCode() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.vouchCode)).setText("单号:" + s.getVouchCode());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.vouchCode)).setText("单号:");
                        }
                        if (s.getInvName() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("货品名称:" + s.getInvName());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.invName)).setText("货品名称:");
                        }
                        if (s.getBatchCode() != null) {
                            ((TextView) CommonViewHolder.get(item, R.id.batchCode)).setText("批次:" + s.getBatchCode());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.batchCode)).setText("批次:");
                        }
                        if (s.getPlanQty() != null){
                            ((TextView) CommonViewHolder.get(item, R.id.tv_plan_qty)).setText("计划数量:" + s.getPlanQty());
                        } else {
                            ((TextView) CommonViewHolder.get(item, R.id.tv_plan_qty)).setText("计划数量:");
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


    private void Close() {

        //关闭Activity
        Intent intent = new Intent();
        intent.putExtra("vouchId", "-1");
        intent.putExtra("vouchCode", "");

        intent.putExtra("depName", "");
        intent.putExtra("depCode", "");
        intent.putExtra("sourceType", "");
        setResult(11, intent);//回传数据到主Activity
        this.finish();
    }
}