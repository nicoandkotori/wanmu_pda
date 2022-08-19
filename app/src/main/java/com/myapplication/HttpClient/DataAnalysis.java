
package com.myapplication.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.myapplication.utils.StringUtil;

import static android.R.attr.id;
import static okhttp3.Protocol.get;


/**
 * Created by Vfun01 on 2017-10-30.
 */

public class DataAnalysis {
    public enum JSON_TYPE {
        /**
         * JSONObject
         */
        JSON_OBJECT,
        /**
         * JSONArray
         */
        JSON_ARRAY,
        /**
         * 不是JSON格式的字符串
         */
        JSON_ERROR
    }

    /**
     * @param result 返回数据
     * @Description 获取result数据格式
     */
    private static JSON_TYPE getJSONType(String result) {
        if (StringUtil.isEmpty(result)) {
            return JSON_TYPE.JSON_ERROR;
        }

        final char[] strChar = result.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if (firstChar == '{') {
            return JSON_TYPE.JSON_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_ARRAY;
        } else {
            return JSON_TYPE.JSON_ERROR;
        }
    }

    /**
     * @param result 请求返回字符串
     * @Description 返回数据解析
     */
    public static ResultDesc getReturnData(String result) {
        ResultDesc resultDesc = null;

        if (StringUtil.isEmpty(result)) {
            //返回数据为空
            resultDesc = dataRestructuring(false, "数据返回异常", "","");
            return resultDesc;
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            //返回码
            boolean success=true;
            String msg="";
            String errormsg="";
            Object res=null;
//           String aa= jsonObject.getClass();
           if(jsonObject.has("success"))
           {
               success = jsonObject.getBoolean("success");
           }

            //返回说明
            if(jsonObject.has("msg")) {
                msg = jsonObject.getString("msg");
            }
            //返回数据
            if(jsonObject.has("errormsg")) {
                errormsg = jsonObject.getString("errormsg");
            }
            //返回数据
            if(jsonObject.has("result")) {
                res= jsonObject.getString("result");
            }

            resultDesc = dataRestructuring(success, msg, errormsg,res);
        } catch (JSONException e) {
            resultDesc = dataRestructuring(false, "数据返回异常", "","");
        }

        return resultDesc;
    }

    /**
     * @param success 返回码
     * @param msg     返回说明
     * @param errormsg     返回数据
     * @param res     res
     * @Description 数据重组
     */

    private static ResultDesc dataRestructuring( boolean success, String msg, String errormsg,Object res) {
        ResultDesc resultDesc = new ResultDesc(success, msg, errormsg,res);
        return resultDesc;
    }
}
