package com.zchd.library.WebViewUtil;

import java.lang.reflect.Method;

/**
 * Created by GJ on 2017/6/15.
 */
public class WebUtil {
    public static boolean isOverriedMethod(Object currentObject, String methodName, String method, Class... clazzs) {
        LogUtils.i("Info", "currentObject:" + currentObject + "  methodName:" + methodName + "   method:" + method);
        boolean tag = false;
        if (currentObject == null)
            return tag;

        try {

            Class clazz = currentObject.getClass();
            Method mMethod = clazz.getMethod(methodName, clazzs);
            String gStr = mMethod.toGenericString();


            tag = !gStr.contains(method);
        } catch (Exception igonre) {
            igonre.printStackTrace();
        }

        LogUtils.i("Info", "isOverriedMethod:" +tag);
        return tag;
    }
}
