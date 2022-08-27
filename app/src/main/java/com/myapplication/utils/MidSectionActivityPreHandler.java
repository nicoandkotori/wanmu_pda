package com.myapplication.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.MidSectionActivity;
import com.myapplication.SearchMoOrderActivity;
import com.myapplication.dto.mo.MoOrderInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.myapplication.Scan.Sound.context;

/**
 * 进入MidSectionActivity预处理程序
 *
 * @author mijiahao
 * @date 2022/08/25
 */
public class MidSectionActivityPreHandler implements PreHandle{


    private Fragment fragment;
    private final Class<MidSectionActivity> targetActivityClazz = MidSectionActivity.class;
    private final String targetActivityName = MidSectionActivity.class.getSimpleName();

    /**
     * 转挂直接进入就行
     */
    @Override
    public void handle() {
        Intent intent = new Intent(context,targetActivityClazz);
        fragment.startActivity(intent);
    }
    

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
