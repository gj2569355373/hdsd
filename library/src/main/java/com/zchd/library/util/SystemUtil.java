package com.zchd.library.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by chenboling on 16/8/19.
 */
final public class SystemUtil {

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 获取系统时间
     */
    public static String getTime(Application application){
        String label = DateUtils.formatDateTime(
                application,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        return label;
    }
    /**
     * 拨打电话
     * <uses-permission android:name="android.permission.CALL_PHONE"></uses-permission>
     * */
    public static void playphone(String number, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//checkSelfPermission需要在最低api 23的版本里使用：
            if(activity.checkSelfPermission(Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED) {
                callPhone(number,activity);
            }else{
                Log.e("err","no");
            }
        }else{
            callPhone(number,activity);
        }
    }
     private static void callPhone(String number, Activity activity) {
         Intent callIntent = new Intent(Intent.ACTION_CALL);
         callIntent.setData(Uri.parse("tel:"+number));
         try {
             activity.startActivity(callIntent);
         }catch (Exception e)
         {
             e.printStackTrace();
         }
     }
    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
