
package com.myapplication.HttpClient;
import android.graphics.Bitmap;

import com.myapplication.R;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import com.myapplication.HttpClient.OkHttpRequest.HttpMethodType;

import com.myapplication.HttpClient.Platform;
/**
 * Created by Vfun01 on 2017-10-30.
 */

public class OkHttpUtils {
    private static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Platform mPlatform;

    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS);
//        mOkHttpClient = builder.build();
        mPlatform = Platform.get();
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void sendFailResultCallback(final int code, final String message, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(code, message);
            }
        });
    }

    public static void sendSuccessResultCallback(final ResultDesc result, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }

    public static void sendProgressResultCallback(final long currentTotalLen, final long totalLen, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(currentTotalLen, totalLen);
            }
        });
    }

    public static void sendBitmapSuccessResultCallback(final Bitmap bitmap, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onBitmapSuccess(bitmap);
            }
        });
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, Map<String, String> params, HttpCallback callback) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doExecute(request, callback);
    }


    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postSync(String url, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(HttpMethodType.POST, url, params, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, Map<String, String> params, HttpCallback callback) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(url, params);
        }
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }
//    /**
//     * @param url      请求地址
//     * @param params   请求参数
//     * @param callback 请求回调
//     * @Description GET请求
//     */
//    public static void getAsyn(String url,String params, HttpCallback callback) {
//
//        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, params);
//        OkHttpRequest.doEnqueue(request, callback);
//    }
    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postAsyn(String url, Map<String, String> params, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(HttpMethodType.POST, url, params, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param json     json数据格式
     * @param callback 请求回调
     * @Description POST提交JSON数据
     */
    public static void postAync(String url, String json, HttpCallback callback) {
        Request request = OkHttpRequest.builderRequest(HttpMethodType.POST, url, null, json);
        OkHttpRequest.doEnqueue(request, callback);
    }


    /**
     * @param tag 请求标签
     * @Description 取消请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        synchronized (mOkHttpClient.dispatcher().getClass()) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
