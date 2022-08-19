package com.myapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * 预处理接口
 *
 * @author mijiahao
 * @date 2022/08/10
 */
public interface PreHandle {

    /**
     * 处理
     */
    void handle();

    void setIntent(Intent intent);

    void setFragment(Fragment fragment);
}
