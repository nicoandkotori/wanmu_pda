
package com.myapplication.HttpClient;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Vfun01 on 2017-10-27.
 */

public class HttpClient extends AppCompatActivity {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private OkHttpClient mOkHttpClient;
    public  void initOkHttpClient() {
//        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS);
//                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }
    public void getAsynHttp() {

        Request.Builder requestBuilder = new Request.Builder().url("http://192.168.1.113:8085/HelloWorld");
//        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d("okHttp", json);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        goods m=new goods();
//                        m.setGoodName("1");
//                        m.setId("1");
//                        goods m1=new goods();
//                        m1.setGoodName("2");
//                        m1.setId("2");
//                        ArrayList<goods> list=new ArrayList<goods>();
//                        list.add(m);
//                        list.add(m1);
//                        BinderListData(list);
//                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void  getRequest() {
        final Request request=new Request.Builder()
                .get()
//                .url("http://192.168.1.141:8085/HelloWorld")
//                .url("http://192.168.1.141:8080/system/menu/getMenuList.action")
//                .url("http://apis.juhe.cn/mobile/get?phone=%22+string+%22&key=%22%20+%222f121eb8bf260e938df638ec3cc2e5d4")
                .url("http://192.168.1.113:8085/HelloWorld")
                .build();
//        client.newCall(request).enqueue(callback);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Response response = null;
                    response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
//                        Toast.makeText(view.getContext(),"业务逻辑写在Fragment上，Activity是不是很整洁了？",Toast.LENGTH_SHORT).show();
                        Log.i("WY","打印GET响应的数据：" + response.body().string());


                    } else {
                        throw new IOException("Unexpected code " + response);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }).start();


    }
    public void requestBlog() {
        String url = "http://192.168.1.113:8085/HelloWorld";

        Request request = new Request.Builder().url(url).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("okHttp", "false");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d("okHttp", json);
            }
        });


    }
    private Response _getAsyn(String url) throws IOException
    {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }
    private void GetData()
    {
        String url = "http://192.168.1.113:8085/HelloWorld";

        Request request = new Request.Builder().url(url).build();

    }

}
