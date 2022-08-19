
package com.myapplication;

import java.util.ArrayList;

import com.zltd.decoder.Constants;
import com.zltd.industry.ScannerManager;

import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;


public abstract class BaseActivity extends FragmentActivity implements
        ScannerManager.IScannerStatusListener {
    protected static final int UPDATE_LIST = 0x1000;
    protected static final int UPDATE_NUMBER = 0x1001;

    protected ScannerManager mScannerManager;
    protected SoundUtils mSoundUtils;



    protected int pressed = 0;
    protected int scanned = 0;
    protected int decoderType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //1.����ScannerManager
        mScannerManager = ScannerManager.getInstance();
        decoderType = mScannerManager.getDecoderType();
        mSoundUtils = SoundUtils.getInstance();
        mSoundUtils.init(this);

    }

    public void onResume() {
        super.onResume();
        //2.����ɨ�����
        int res = mScannerManager.connectDecoderSRV();
        mScannerManager.addScannerStatusListener(this);
        if(decoderType == Constants.DECODER_ONED_SCAN){
            if (!mScannerManager.getScannerEnable()) {
                new AlertDialog.Builder(this)
                .setTitle("2131099649")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("2131099672")
                .setPositiveButton("2131099673", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        closeSelf();
                    }
                })
                .setCancelable(false)
                .show();
            }
        }
    }
 // 4.��App�����������˵���̨֮��,�б�Ҫ�ͷ���Դ,�رջ����Ƴ�ɨ�����
    public void onPause() {
        mScannerManager.removeScannerStatusListener(this);
        mScannerManager.disconnectDecoderSRV();
        super.onPause();
    }

    protected void closeSelf() {
        this.finish();
    }
//����uI
    @SuppressLint("HandlerLeak")
    protected Handler mHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_LIST:
                    scanned++;
//                    mSoundUtils.success();
                   int aa= updateList((String) msg.obj);
                    if(aa==0)
                    {
                        mSoundUtils.success();
                    }
                    else  if(aa==1)
                    {
                        mSoundUtils.warn();
                    }
                default:
                    break;
            }
        }
    };


    public abstract int updateList(String data);

  //3.���һ��ɨ��ص��ӿ�,��������ɨ�践������
    @Override
    public void onScannerResultChanage(byte[] arg0) {
        String data = new String(arg0);
        Message msg = mHandle.obtainMessage(UPDATE_LIST, data);
        mHandle.sendMessage(msg);
    }

    @Override
    public void onScannerStatusChanage(int arg0) {
        // TODO Auto-generated method stub

    }
}
