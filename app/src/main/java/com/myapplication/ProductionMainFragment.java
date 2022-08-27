package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myapplication.Adapter.ContactsAdapter;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.ItemDecoration.MyItemDecoration;
import com.myapplication.dto.System.MenuVM;
import com.myapplication.utils.EnterActivityPreHandler;
import com.myapplication.utils.HVListView;
import com.myapplication.utils.PreHandle;
import com.myapplication.utils.StringUrl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.myapplication.Scan.Sound.context;


public class ProductionMainFragment extends Fragment {
    private LayoutInflater mInflater;
    private List<MenuVM> menuList=new ArrayList<>();
    private HVListView mListView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fragment_production_main,
                container, false);

        context=getContext();

        //找到RecyclerView控件
        final RecyclerView home_rv = (RecyclerView) messageLayout.findViewById(R.id.home_rv);

        //加载功能框和图片
        Map<String, String> mapquery = new HashMap<String, String>();
        mapquery.put("menuName","生产管理");
        OkHttpUtils.postAsyn(StringUrl.GetUrl()+"/api/login/getmenulist",  mapquery,new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                try {
                    if(resultDesc.getresult()!=null)
                    {
                        //获取后台返回的数据
                        menuList = JSON.parseArray(resultDesc.getresult().toString(), MenuVM.class);
                        //实例化Adapter并且给RecyclerView设上
                        final ContactsAdapter adapter = new ContactsAdapter(menuList);
                        adapter.setOnItemClickLitener(new ContactsAdapter.OnItemClickLitener()
                        {
                            @Override
                            public void onItemClick(View view, int position)
                            {

                                initFragment(view,position);

                            }
                        });
                        home_rv.setAdapter(adapter);
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


       /* menuList =new ArrayList<>();
        MenuVM menuVM=new MenuVM();
        menuVM.setPdaMenuIcon("assemble_select");
        menuVM.setPdaMenuName("屠宰称重排队");
        menuVM.setPdaMenuUrl("com.myapplication.WeighQueueActivity");
        menuList.add(menuVM);*/

        //实例化Adapter并且给RecyclerView设上
       /* final ContactsAdapter adapter = new ContactsAdapter(menuList);
        adapter.setOnItemClickLitener(new ContactsAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

                initFragment(view,position);

            }
        });
        home_rv.setAdapter(adapter);*/


        // 如果我们想要一个GridView形式的RecyclerView，那么在LayoutManager上我们就要使用GridLayoutManager
        // 实例化一个GridLayoutManager，列数为3
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        //final LinerLayoutManager linerLayoutManager=new LinerLayoutManager(getActivity());
        //调用以下方法让RecyclerView的第一个条目仅为1列
       /* layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //如果位置是0，那么这个条目将占用SpanCount()这么多的列数，再此也就是3
                //而如果不是0，则说明不是Header，就占用1列即可
                return adapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
            }
        });*/
        //把LayoutManager设置给RecyclerView
        home_rv.setLayoutManager(layoutManager);
        home_rv.addItemDecoration(new MyItemDecoration());
        int n=  home_rv.getBaseline();

        return messageLayout;
    }


    private void initFragment(View view,int position) {
        try {
            TextView value_tv = (TextView) view.findViewById(R.id.value_tv);
            String activityClassName = value_tv.getText().toString();
            Class cls = Class.forName(activityClassName);
            Intent intent = new Intent(context, cls);
            //查询当天的批次，如果只有一个批次信息，直接进入activity，否则跳转到批次查询页面选择批次
            EnterActivityPreHandler preHandler = EnterActivityPreHandler.getInstance();
            preHandler.preHandle(cls,this);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 进入Activity前进行处理
     */
    private void preHandleBeforeEnterActivity(PreHandle preHandle){
        preHandle.handle();
    }



    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                String str = bundle.getString("backData");
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
