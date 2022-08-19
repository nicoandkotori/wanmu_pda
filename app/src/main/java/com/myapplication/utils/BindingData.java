package com.myapplication.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Method;


import java.lang.reflect.Field;

/**
 * Created by Vfun01 on 2017-11-22.
 */

public class BindingData {
    /**
     * 绑定对应属性值到TextView和EditText控件上
     * 规则:
     * TextView控件, myProperty-->txt_myProperty
     * EditText控件, myProperty-->etxt_myProperty
     *
     * @param root
     * @param bean
     */
    public static void bind(View root, Object bean) {
        if (root == null || bean == null) {
            return;
        }
        if (root instanceof ViewGroup) {
            View child;
            String tmp = null;
            Object arg = null;
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                child = ((ViewGroup) root).getChildAt(i);
                if (child instanceof TextView) {
                    try {
                        tmp = child.getResources().getResourceName(child.getId());
                    } catch (Exception ex) {
//                        LogEnrising.i("发现没有设置id属性的TextView控件");
                        continue;
                    }
                    tmp = tmp.substring(tmp.indexOf("/") + 1);

                    Field[] fields = bean.getClass().getDeclaredFields();
                    for (Field field : fields) {

                        if (tmp.equals( field.getName())) {
                            field.setAccessible(true);
                            try {
                                if (field.getGenericType().toString().equals(
                                        "class java.lang.String") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Double") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Integer") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Short")) {
                                    Method m = (Method) bean.getClass().getMethod(
                                            field.getName());

                                    arg = m.invoke(bean);// 调用getter方法获取属性值
                                }
                            } catch (Exception ee) {
//                                LogEnrising.i("反射取值出错:" + field.getName());
                            }

                            ((TextView) child).setText(String.valueOf(arg));
                            break;
                        }
                    }

                } else if (child instanceof EditText) {
                    try {
                        tmp = child.getResources().getResourceName(child.getId());
                    } catch (Exception ex) {
//                        LogEnrising.i("发现没有设置id属性的EditText控件");
                        continue;
                    }
                    tmp = tmp.substring(tmp.indexOf("/") + 1);

                    Field[] fields = bean.getClass().getDeclaredFields();
                    for (Field field : fields) {

                        if (tmp.equals( field.getName())) {
                            field.setAccessible(true);
                            try {
                                if (field.getGenericType().toString().equals(
                                        "class java.lang.String") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Double") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Integer") ||
                                        field.getGenericType().toString().equals(
                                                "class java.lang.Short")) {
                                    Method m = (Method) bean.getClass().getMethod(
                                           field.getName());

                                    arg = m.invoke(bean);// 调用getter方法获取属性值
                                }
                            } catch (Exception ee) {
//                                LogEnrising.i("反射取值出错:" + field.getName());
                            }

                            ((EditText) child).setText(String.valueOf(arg));
                            break;
                        }
                    }
                } else if (child instanceof ViewGroup) {
                    bind(child, bean);
                }
            }
        } else {
        }
    }

//    // 把一个字符串的第一个字母大写、效率是最高的、
//    private static String getMethodName(String fildeName) throws Exception {
//        byte[] items = fildeName.getBytes();
//        items[0] = (byte) ((char) items[0] - 'a' + 'A');
//        return new String(items);
//    }
}
