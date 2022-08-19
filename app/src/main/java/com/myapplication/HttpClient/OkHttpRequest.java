package com.myapplication.HttpClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.myapplication.R;
import com.myapplication.utils.StringUrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okio.BufferedSink;

import static android.R.attr.key;


/**
 * Created by Vfun01 on 2017-10-30.
 */

public class OkHttpRequest {
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//JSON数据格式
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");//二进制流数据
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");//纯文本格式

    private static OkHttpClient getOkHttpClient() {
        return OkHttpUtils.getInstance().getOkHttpClient();
    }

    /**
     * @Description 请求方式
     */
    enum HttpMethodType {
        GET,
        POST,
    }

    /**--------------------    GET请求参数拼接    --------------------**/

    /**
     * @param url    请求地址
     * @param params 请求参数
     * @Description GET请求参数拼接
     */
    public static String appendGetParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }

        return builder.build().toString();
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 同步数据请求
     */
    public static void doExecute(final Request request, HttpCallback callback) {
        try {
            Response response = getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                OkHttpUtils.sendSuccessResultCallback(DataAnalysis.getReturnData(response.body().string()), callback);
            } else {
                OkHttpUtils.sendFailResultCallback(response.code(), response.message(), callback);
            }
        } catch (Exception e) {
            OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
        }
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param methodType 请求方式
     * @param url        请求地址
     * @param params     请求参数
     * @param json       json数据格式
     * @Description Request对象
     */
    public static Request builderRequest(HttpMethodType methodType, String url, Map<String, String> params, String json) {
        Request.Builder builder = new Request.Builder()
                .header("token", StringUrl.Gettoken())
                .url(url);

        if (methodType == HttpMethodType.POST) {
            if (json != null) {

                RequestBody.create(MEDIA_TYPE_JSON, json);
                RequestBody body =builderJson(json) ;
                builder.post(body);
            } else {

                RequestBody body = builderFormData(params);
                builder.post(body);
            }
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
        }

        return builder.build();
    }

    /**
     * @param params 请求参数
     * @Description RequestBody对象
     */
    private static RequestBody builderFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        StringBuffer sb = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
                sb.append(key+"="+params.get(key)+"&");
            }
        }

        RequestBody.create(MEDIA_TYPE_JSON, sb.toString());
        return builder.build();
    }
    /**

     * @Description RequestBody对象
     */
    private static RequestBody builderJson(String json) {
        FormBody.Builder builder = new FormBody.Builder();

        if (json != null && !json.isEmpty()) {

                builder.add("query", json);

        }

        return builder.build();
    }
    /**
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 异步请求
     */
    public static void doEnqueue(final Request request, final HttpCallback callback) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        OkHttpUtils.sendSuccessResultCallback(DataAnalysis.getReturnData(response.body().string()), callback);
                    } else {
                        OkHttpUtils.sendFailResultCallback(response.code(), response.message(), callback);
                    }
                } catch (Exception e) {
                    OkHttpUtils.sendFailResultCallback(-1, e.getMessage(), callback);
                }
            }
        });
    }




    /**--------------------    异步流式提交    --------------------**/

    /**
     * @param url     请求地址
     * @param content 提交内容
     * @Description Request对象
     */
    public static Request builderStreamRequest(String url, final String content) {
        Request.Builder builder = new Request.Builder()
                .header("token", StringUrl.Gettoken())
                .url(url);

        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_JSON;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(content);
            }

            @Override
            public long contentLength() throws IOException {
                return content.length();
            }
        };
        builder.post(body);

        return builder.build();
    }



    /**
     * @param builder Request.Builder
     * @param name    名称
     * @param value   值
     * @Description 添加单个头部信息
     */
    public static Request.Builder appendHeader(Request.Builder builder, String name, String value) {
        builder.header(name, value);
        return builder;
    }

    /**
     * @param builder Request.Builder
     * @param headers 头部参数
     * @Description 添加多个头部信息
     */
    public static Request.Builder appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return builder;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
        return builder;
    }
}
