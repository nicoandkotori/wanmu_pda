package com.myapplication.utils;


import java.util.List;

/**
 * Created by Vfun01 on 2017-11-30.
 */

public class StringUrl {
    public static String NowWhCode;
    public static String NowUserName;
    public static String NowUserId;
    public static String NowToken;
    public static String ip;//服务器IP
    public static String port;//服务器端口号
    public static String printer;//打印机名
    //服务器连接地址

    //服务器连接地址
    public static String GetUrl() {
        return "http://" + ip + ":" + port;
//        return "http://192.168.1.50:8088/Test";
//        return "http://jxwinner.ufyct.com:8000";
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        StringUrl.ip = ip;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        StringUrl.port = port;
    }
    public static String getPrinter() {
        return printer;
    }

    public static void setPrinter(String printer) {
        StringUrl.printer = printer;
    }
    //登录用户
    public static void SetUser(String UserName) {

        NowUserName=UserName;

    }
    //登录用户
    public static void SetUserId(String UserId) {

        NowUserId=UserId;

    }
    public static String GetUser() {

        if(!StringUtil.isEmpty(NowUserName))
        {
            return  NowUserName;
        }
        else
        {
            return"demo";
        }
    }

    public static String GetUserId() {

        if(!StringUtil.isEmpty(NowUserId))
        {
            return  NowUserId;
        }
        else
        {
            return"";
        }
    }
    //登录用户
    public static void Settoken(String token) {

        NowToken=token;

    }
    public static String Gettoken() {

        if(!StringUtil.isEmpty(NowToken))
        {
            return  NowToken;
        }
        else
        {
            return"";
        }
    }


    //仓库
    public static void SetWhCode(String whCode) {

        NowWhCode=whCode;

    }
    public static String GetWhCode() {

        if(!StringUtil.isEmpty(NowWhCode))
        {
            return  NowWhCode;
        }
        else
        {
            return"";
        }
    }

    public static String getVouchType(String query) {

        if(query.equals("OutUse"))
        {
            return "领料出库单";
        }
        else if(query.equals("PurchaseOut"))
        {
            return "采购退货单";
        }
        else if(query.equals("OtherOut"))
        {
            return "其他出库单";
        }
        else
        {
            return"";
        }


    }

    public static String getVouchCode(String query) {

        if(query.equals("OutUse"))
        {
            return "MO";
        }
        else if(query.equals("PurchaseOut"))
        {
            return "PR";
        }
        else if(query.equals("OtherOut"))
        {
            return "OO";
        }
        else
        {
            return "";
        }

    }
}
