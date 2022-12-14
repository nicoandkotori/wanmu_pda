package com.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myapplication.HttpClient.HttpCallback;
import com.myapplication.HttpClient.OkHttpUtils;
import com.myapplication.HttpClient.ResultDesc;
import com.myapplication.download.DownloadService;
import com.myapplication.download.FileProviderUtil;
import com.myapplication.download.VersionUtil;
import com.myapplication.dto.System.User;
import com.myapplication.utils.CustomStringUtils;
import com.myapplication.utils.SharedPreferencesUtil;
import com.myapplication.utils.StringUrl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by WZH on 2017/3/25.
 */

public class LoginActivity extends FragmentActivity implements View.OnClickListener  {
    private ImageView logo;
    private ScrollView scrollView;
    private EditText et_mobile;
    private EditText et_password;
    private ImageView iv_clean_phone;
    private ImageView clean_password;
    private ImageView iv_show_pwd;
    private Button btn_login;
    private Button setupServer;

    private TextView setupServer1;
    private TextView forget_password;
    protected static final int LOWER_VERSION = 0;
    protected static final int EQUAL_VERSION = 1;
    protected static final int HIGHER_VERSION = 2;
    protected static final int NETWORK_ERROR = 3;

    private int screenHeight = 0;//????????????
    private int keyHeight = 0; //??????????????????????????????
    private float scale = 0.6f; //logo????????????
    private View service,content;
    private TextView startDownload;
    //??????????????????
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if(isFullScreen(this)){
            AndroidBug5497Workaround.assistActivity(this);
        }
        //??????????????????????????????????????????
        initServerInfo();
        initOkHttp();
        initView();
        initListener();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //????????????activity
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            finish();
            return;
        }
        et_mobile.setText("");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initServerInfo() {
        String ip = SharedPreferencesUtil.getFromFile(this,"ip");;
        String port = SharedPreferencesUtil.getFromFile(this,"port");
        String printer = SharedPreferencesUtil.getFromFile(this,"printer");
        StringUrl.setIp(ip);
        StringUrl.setPort(port);
        StringUrl.setPrinter(printer);
    }

    public boolean isFullScreen(Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN)==WindowManager.LayoutParams.FLAG_FULLSCREEN;
    }
    private void initView() {
        logo = (ImageView) findViewById(R.id.logo);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_clean_phone = (ImageView) findViewById(R.id.iv_clean_phone);
        clean_password = (ImageView) findViewById(R.id.clean_password);
        iv_show_pwd = (ImageView) findViewById(R.id.iv_show_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
        setupServer = (Button) findViewById(R.id.setupServer);
        setupServer1 = (TextView) findViewById(R.id.setupServer1);
        setupServer1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //?????????
        setupServer1.getPaint().setAntiAlias(true);//?????????
        content = findViewById(R.id.content);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //??????????????????
        keyHeight = screenHeight / 3;//??????????????????????????????1/3
        service = findViewById(R.id.service);
        startDownload= (TextView) findViewById(R.id.start_download);
        startDownload.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //?????????
        startDownload.getPaint().setAntiAlias(true);//?????????
        //????????????
        progressDialog =new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("????????????...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }

    /**
     * ????????????
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    private void initListener() {
        iv_clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        //????????????
        startDownload.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(LoginActivity.this,"????????????????????????", Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    et_password.setSelection(s.length());
                }
            }
        });
        /**
         * ???????????????????????????????????????
         */
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });



        //????????????
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(CustomStringUtils.isBlank(StringUrl.getIp()) ||
                        CustomStringUtils.isBlank(StringUrl.getPort()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("???????????????????????????!");
                    builder.setPositiveButton("??????",null);
                    builder.show();
                    return;
                }
                 User u=new User();
                u.setUserCode(et_mobile.getText().toString());
                u.setPwd(et_password.getText().toString());
//                if(u.getUsercode().equals("gl")&&u.getPassword().equals("gl"))
//                {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
                String query= JSON.toJSONString(u);
                Map<String, String> mapquery = new HashMap<String, String>();
                mapquery.put("username",et_mobile.getText().toString());
                mapquery.put("password",et_password.getText().toString());
                OkHttpUtils.postAsyn(StringUrl.GetUrl()+"/api/login/checkLogin",mapquery,new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);

                        if(resultDesc.getsuccess()==true)
                        {
//                            User m= JSON.parseObject(resultDesc.getresult().toString(),User.class);
//                            StringUrl.SetUser(m.getUserName());
                            User m=JSON.parseObject(resultDesc.getresult().toString(),User.class);
                            StringUrl.SetUser(m.getUserName());
                            StringUrl.SetUserId(m.getId());
                            StringUrl.Settoken(m.getId());
                            StringUrl.SetWhCode(m.getWhCode());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {

                            Toast.makeText(LoginActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        Toast.makeText(LoginActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        // ???????????????
        setupServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ServerSetupActivity.class);
                startActivityForResult(intent, 1);//
            }
        });

        // ???????????????
        setupServer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ServerSetupActivity.class);
                startActivityForResult(intent, 1);//
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_download:
                downloadApk();
                break;
            case R.id.iv_clean_phone:
                et_mobile.setText("");
                break;
            case R.id.clean_password:
                et_password.setText("");
                break;
            case R.id.iv_show_pwd:
                if (et_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pwd.setImageResource(R.drawable.pass_visuable);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pwd.setImageResource(R.drawable.pass_gone);
                }
                String pwd = et_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    et_password.setSelection(pwd.length());
                break;
        }
    }

    //??????Apk
    private void downloadApk() {
        try {
            if (CustomStringUtils.isBlank(StringUrl.getIp()) ||
                    CustomStringUtils.isBlank(StringUrl.getPort())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("???????????????????????????!");
                builder.setPositiveButton("??????", null);
                builder.show();
                return;
            }
            //??????apk???????????????????????????????????????????????????
            //1.??????????????????????????????
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int permission = checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
                //???????????????????????????????????????
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                            {"android.permission.READ_EXTERNAL_STORAGE",
                                    "android.permission.WRITE_EXTERNAL_STORAGE" }, 1);
                }else {
                    //??????APK
                    compareVersionCode();
                }
            }
        }catch (Exception e){
            progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    //??????Handler??????????????????UI??????)
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOWER_VERSION:
                    updateApk();
                    break;
                case EQUAL_VERSION:
                    Toast.makeText(LoginActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case HIGHER_VERSION:
                    Toast.makeText(LoginActivity.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(LoginActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    };


    /**
     * ???????????????
     */
    public void compareVersionCode(){
        new Thread(){
            public void run() {
                Message msg = Message.obtain();
                try {
                    //???????????????
                    int newVersionCode = VersionUtil.getServerVersion();
                    if(VersionUtil.getCurVersionCode(LoginActivity.this) < newVersionCode){
                        //????????????
                        msg.what = LOWER_VERSION;
                    } else if(VersionUtil.getCurVersionCode(LoginActivity.this) == newVersionCode){
                        msg.what = EQUAL_VERSION;
                    } else{
                        msg.what = HIGHER_VERSION;
                    }
                } catch (Exception e) {
                    msg.what = NETWORK_ERROR;
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void updateApk() {
        progressDialog.show();
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", "http://"+StringUrl.getIp() + ":79/xiaolaoban.apk");
//        intent.putExtra("url","http://dingphone.ufile.ucloud.com.cn/apk/guanwang/time2plato.apk");
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //????????????
                compareVersionCode();
            } else {
                Toast.makeText(LoginActivity.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                //????????????
            }
        }

    }

    private void initOkHttp() {
        File cache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)//????????????(??????:???)
                .writeTimeout(200, TimeUnit.SECONDS)//????????????(??????:???)
                .readTimeout(200, TimeUnit.SECONDS)//????????????(??????:???)
                .cache(new Cache(cache.getAbsoluteFile(), cacheSize))//????????????
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    //??????ResultReceiver????????????DownloadService?????????????????????
    public class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            switch (resultCode){
                case DownloadService.UPDATE_PROGRESS: {
                    int progress = resultData.getInt("progress");
                    String filePath = resultData.getString("filePath");
                    //(true)?????????????????????????????????????????????????????????
                    //(false)?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    progressDialog.setIndeterminate(false);
                    progressDialog.setProgress(progress);
                    if (progress == 100) {
                        progressDialog.dismiss();
                        //?????????????????????apk
                        File file = new File(filePath);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        if (file.exists()) {
                            FileProviderUtil.installAPK(LoginActivity.this, installIntent,
                                    "application/vnd.android.package-archive", file, true);
                            startActivity(installIntent);
                        } else {
                            Toast.makeText(LoginActivity.this, "????????????,?????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case DownloadService.UPDATE_ERROR:{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    }

}
