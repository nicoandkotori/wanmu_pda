package com.myapplication.download;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//在这里根据url进行下载文件，并通过receiver把需要更新的progressbar的值放在bundle传过去
public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    public static final int UPDATE_ERROR = 8345;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        HttpURLConnection connection ;
        InputStream input = null;
        OutputStream output = null;
        try {
            URL url = new URL(urlToDownload);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            // download the file
            //根据URL解析出下载的文件名
            String fileName=urlToDownload.substring(urlToDownload.lastIndexOf("/"));
            //将文件下载到Environment.DIRECTORY_DOWNLOADS目录下，也就是SD卡的Download目录
//            String directory= Environment.getExternalStorageDirectory().getPath();
            String directory = "/sdcard";
            String filePath = directory + fileName;
            File file=new File(filePath);
            //判断目录中是否已经存在要下载的文件
            if (file.exists()){
                file.delete();
            }
            input = connection.getInputStream();
            output = new FileOutputStream(filePath);
            byte data[] = new byte[2048];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                //进度条进度情况显示不对，解决：边下载边广播消息
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                resultData.putString("filePath",file.getPath());
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
            //下载出错后进度条仍然显示在界面上，解决：广播出错消息
            Bundle resultData = new Bundle();
            receiver.send(UPDATE_ERROR, resultData);
        } finally {
            try {
                if(input != null){
                    input.close();
                }
                if(output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
