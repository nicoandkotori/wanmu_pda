package com.myapplication.Search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.alibaba.fastjson.JSON;
import com.myapplication.Adapter.DepartmentSearchAdapter;
import com.myapplication.Adapter.WarehouseSearchAdapter;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.R;
import com.myapplication.dto.basicinfo.Department;
import com.myapplication.dto.basicinfo.Warehouse;
import com.myapplication.utils.StringUrl;
import com.myapplication.utils.TitleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Vfun01 on 2017-11-16.
 */

public class DepartmentSearch extends FragmentActivity {

    private SearchView mSearchView;
    private ListView mListView;
    List<Department> mdatas;
    private Handler handler;
    private DepartmentSearchAdapter adapter;
    private TitleView titleView;
    private ImageView mIvLeftImage;     // 左边的图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search_department);
            mSearchView = (SearchView) findViewById(R.id.searchView);
            mListView = (ListView) findViewById(R.id.listView);

            //标题
            titleView = (TitleView) findViewById(R.id.titleView);
            titleView.setAppTitle("部门查询");
            mIvLeftImage = (ImageView) findViewById(R.id.iv_left_image);
            mIvLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Close();
                }
            });
            //标题
            ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
            tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));


            //仓库列表信息加载
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.arg1==99)
                    {
                        adapter = new DepartmentSearchAdapter(getBaseContext(), mdatas);
                        mListView.removeAllViewsInLayout();
                        mListView.setAdapter(adapter);
                    }


                }
            };


            //列表信息查询
            Map<String, String> mapquery = new HashMap<String, String>();
            //列表信息查询
            OkHttpUtils.postAsyn(StringUrl.GetUrl()+"/api/basicinfo/department/getlistbypda",mapquery,new HttpCallback() {
                @Override
                public void onSuccess(ResultDesc resultDesc) {
                    super.onSuccess(resultDesc);
                    mdatas= JSON.parseArray(resultDesc.getresult().toString(), Department.class);
                    Message message = Message.obtain();
                    message.arg1 =99 ;
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(int code, String message) {
                    super.onFailure(code, message);

                }
            });


            mSearchView.setFocusable(false);



            // 设置搜索文本监听
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                // 当点击搜索按钮时触发该方法
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                // 当搜索内容改变时触发该方法
                @Override
                public boolean onQueryTextChange(String newText) {

                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {


                    Department map=(Department)mListView.getItemAtPosition(arg2);

                    Intent intent=new Intent();
                    intent.putExtra("depCode",String.valueOf(map.getCdepcode()) );
                    intent.putExtra("depName",String.valueOf(map.getCdepname()) );

                    setResult(2, intent);//回传数据到主Activity
                    finish(); //此方法后才能返回主Activity

                }

            });

            hideInput();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

    @Override

    public void onBackPressed() {

        Close();

    }
    private void Close()
    {
        //数据是使用Intent返回

        Intent intent = new Intent();

        //把返回数据存入Intent

        intent.putExtra("depCode","-1");
        intent.putExtra("depName","");

        //设置返回数据

        this.setResult(2, intent);

        //关闭Activity

        this.finish();
    }
}
