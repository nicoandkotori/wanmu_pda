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
import com.myapplication.StartToHangActivity;
import com.myapplication.dto.mo.MoOrderInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myapplication.Scan.Sound.context;

/**
 * 进入起挂Activity预处理程序
 *
 * @author mijiahao
 * @date 2022/08/25
 */
public class StartToHangActivityPreHandler implements PreHandle{

    private Fragment fragment;

    @Override
    public void handle() {
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
                    Intent intent = new Intent(context, StartToHangActivity.class);
                    intent.putExtra("info",moOrderInfoList.get(0));
                    fragment.startActivity(intent);
                } else if (moOrderInfoList.size()>1){
                    Intent intent = new Intent(context, SearchMoOrderActivity.class);
                    intent.putExtra("targetActivity",StartToHangActivity.class);
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



    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
