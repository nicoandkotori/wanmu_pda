package com.myapplication.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.myapplication.MidSectionActivity;
import com.myapplication.StartToHangActivity;
import com.myapplication.TetradActivity;
import com.myapplication.WeighQueueActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 进入Activity预处理程序，是预处理的入口，包含预处理规则的映射
 *
 * @author mijiahao
 * @date 2022/08/11
 */
public class EnterActivityPreHandler {


    private  Map<Class,PreHandle> map;

    private static EnterActivityPreHandler enterActivityPreHandler;



    private EnterActivityPreHandler(){

    }

    /**
     * 获得单例
     *
     * @return {@link EnterActivityPreHandler}
     */
    public static EnterActivityPreHandler getInstance(){
        if (enterActivityPreHandler == null){
            enterActivityPreHandler = new EnterActivityPreHandler();
            enterActivityPreHandler.init();
            return enterActivityPreHandler;
        }
        return enterActivityPreHandler;
    }


    /**
     * -------------初始化，在此处扩展进入activity前对应的预处理规则-------------
     */
    private void init(){
        if (map == null){
            map = new HashMap<>(16);
            //需要查询批次
            map.put(WeighQueueActivity.class,new WeightQueueActivityPreHandler());
            //需要查询批次
            map.put(StartToHangActivity.class,new StartToHangActivityPreHandler());
            //无需查询批次，直接跳转
            map.put(MidSectionActivity.class,new MidSectionActivityPreHandler());
            //无需查询批次，直接跳转
            map.put(TetradActivity.class,new TetradActivityPreHandler());
        }
    }

    /**
     * 预处理
     *
     * @param clazz    clazz
     * @param fragment fragment
     */
    public void preHandle(Class clazz, Fragment fragment){
        PreHandle preHandle = map.get(clazz);
        assert preHandle != null;
        preHandle.setFragment(fragment);
        preHandle.handle();
    }
}
