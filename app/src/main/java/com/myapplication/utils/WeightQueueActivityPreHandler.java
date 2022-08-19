package com.myapplication.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.SearchMoOrderActivity;
import com.myapplication.WeighQueueActivity;
import com.myapplication.dto.mo.MoOrderInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myapplication.Scan.Sound.context;

/**
 * 主菜单-->屠宰称重Activity预处理程序，负责预处理的具体逻辑实现
 *
 * @author mijiahao
 * @date 2022/08/10
 */
public class WeightQueueActivityPreHandler implements PreHandle {

    private Intent intent;
    private Fragment fragment;

    public WeightQueueActivityPreHandler() {
    }

    @Override
    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public void handle() {
        //由于网络请求无法在主线程中进行，因此会先进入WeighQueueActivity，此时传递一个需要让其等会销毁的flag
        intent.putExtra("destroyFlag",true);
        MoOrderInfo queryData = new MoOrderInfo();
        Map<String,String> queryMap = new HashMap<>();
        //查询当天所有批次
        queryData.setVouchDateStart(DateUntil.getTodayMidNightTime(new Date()));
        queryData.setVouchDateEnd(DateUntil.getTomorrowMidNightTime(new Date()));
        queryMap.put("query", JSON.toJSONString(queryData));
        OkHttpUtils.getAsyn(StringUrl.GetUrl()+"/api/mo/order/getListForWeigh",queryMap,new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                String res=resultDesc.getresult().toString();
                List<MoOrderInfo> moOrderInfoList= JSON.parseArray(res, MoOrderInfo.class);
                if (moOrderInfoList.size() == 1){
                    Log.e(this.getClass().getName(),"查询到批次记录只有一个，进入WeighQueueActivity");
                    Intent intent = new Intent(context, WeighQueueActivity.class);
                    intent.putExtra("info",moOrderInfoList.get(0));
                    fragment.startActivity(intent);
                } else if (moOrderInfoList.size()>1){
                    Intent intent = new Intent(context, SearchMoOrderActivity.class);
                    intent.putExtra("targetActivity",WeighQueueActivity.class);
                    intent.putExtra("info",(Serializable) moOrderInfoList);
                    fragment.startActivity(intent);
                } else {
                    Toast.makeText(fragment.getContext(),"请先在管理端创建屠宰订单！",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);

            }
        });
    }


}
