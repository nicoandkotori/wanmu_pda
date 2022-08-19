package com.myapplication.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.myapplication.StartToHangActivity;
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
     * -------------初始化，在此处扩展clazz对应的预处理规则-------------
     */
    private void init(){
        if (map == null){
            map = new HashMap<>();
            map.put(WeighQueueActivity.class,new WeightQueueActivityPreHandler());
            map.put(StartToHangActivity.class,new StartToHangActivityPreHandler());
        }
    }

    /**
     * 预处理
     *
     * @param clazz    clazz
     * @param intent   intent
     * @param fragment fragment
     */
    public void preHandle(Class clazz,Intent intent, Fragment fragment){
        PreHandle preHandle = map.get(clazz);
        assert preHandle != null;
        preHandle.setIntent(intent);
        preHandle.setFragment(fragment);
        preHandle.handle();
    }
}
