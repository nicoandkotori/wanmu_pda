package com.myapplication.download;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.myapplication.utils.StringUrl;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionUtil {

    /**
     * 获取当前app版本号
     *
     */
    public static Integer getCurVersionCode(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        int versionCode = packInfo.versionCode;
        return versionCode;
    }

    /**
     * 从服务器获取最新的版本信息
     */
    public static Integer getServerVersion() throws IOException, JSONException {
        URL url = new URL("http://"+StringUrl.getIp() + ":79/version.txt");
        //开启一个连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(4000);
        connection.setReadTimeout(4000);
        connection.setRequestMethod("GET");
        if(connection.getResponseCode()==200) {
            InputStream is = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String json = bufferedReader.readLine();
            System.out.println("json----->" + json);
            JSONObject jsonObject = new JSONObject(json);
            String versionCode = jsonObject.getString("versionCode");
            return Integer.valueOf(versionCode);
        }
        return 0;
    }
}
