package com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.utils.BarEntity;
import com.myapplication.utils.BottomTabBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class MainActivity extends FragmentActivity implements BottomTabBar.OnSelectListener{
    private BottomTabBar tb ;
    private List<BarEntity> bars ;
    private MyMainFragment myMainFragment;
    private ProductionMainFragment productionMainFragment ;
    private AssembleMainFragment assembleMainFragment ;
    private WarehouseMainFragment warehouseMainFragment ;
    private FragmentManager manager ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        manager = getSupportFragmentManager();
        tb = (BottomTabBar) findViewById(R.id.tb);
        bars = new ArrayList<>();

        bars.add(new BarEntity("生产管理",R.drawable.production_select,R.drawable.production_default));
        bars.add(new BarEntity("库存管理",R.drawable.warehouse_select,R.drawable.warehouse_default));
        bars.add(new BarEntity("其他业务",R.drawable.assemble_select,R.drawable.assemble_default));
        bars.add(new BarEntity("我的",R.drawable.home_my_unselect,R.drawable.home_my_select));
        tb.setManager(manager).setOnSelectListener(this).setBars(bars);
    }

    @Override
    public void onSelect(int position) {
        switch (position){
            case 0:
                if (productionMainFragment==null){
                    productionMainFragment = new ProductionMainFragment();
                }
                tb.switchContent(productionMainFragment);
                break;

            case 1:
                if (warehouseMainFragment==null){
                    warehouseMainFragment = new WarehouseMainFragment();
                }
                tb.switchContent(warehouseMainFragment);
                break;


            case 2:
                if (assembleMainFragment==null){
                    assembleMainFragment = new AssembleMainFragment();
                }
                tb.switchContent(assembleMainFragment);
                break;
            case 3:
                if (myMainFragment==null){
                    myMainFragment = new MyMainFragment();
                }
                tb.switchContent(myMainFragment);
                break;

            default:
                break;
        }
    }

    private void initOkHttp() {
        File cache = getExternalCacheDir();
        int cacheSize = 100 * 1024 * 1024;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)//连接超时(单位:秒)
                .writeTimeout(200, TimeUnit.SECONDS)//写入超时(单位:秒)
                .readTimeout(200, TimeUnit.SECONDS)//读取超时(单位:秒)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//设置缓存
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private long exitTime = 0;
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}