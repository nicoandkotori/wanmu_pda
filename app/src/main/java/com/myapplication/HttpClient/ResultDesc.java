package com.myapplication.HttpClient;

import static android.R.attr.data;
import static android.R.attr.id;

/**
 * Created by Vfun01 on 2017-10-30.
 */

public class ResultDesc {
    private boolean success;//返回码
    private String msg;//返回说明
    private String errormsg;//返回数据
    private Object result;
    public ResultDesc(boolean success, String msg, String errormsg,Object result) {
        this.success = success;
        this.msg = msg;
        this.errormsg = errormsg;
        this.result=result;
    }

    public boolean getsuccess() {
        return success;
    }

    public void setsuccess(boolean success) {
        this.success = success;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String geterrormsg() {
        return errormsg;
    }

    public void seterrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
    public Object getresult() {
        return result;
    }

    public void setresult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultDesc{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
