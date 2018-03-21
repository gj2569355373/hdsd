
package com.zchd.library.phone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

public class Utils {

    /**
     * 直接显示Toast
     * 
     * @param context 当前环境上下文对象
     * @param text 内容
     * @param isShort 是否短时间显示（false则为长时间显示）
     */
    public static void showToast(Context context, String text, boolean isShort) {
        if (isShort) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接显示Toast
     * 
     * @param context 当前环境上下文对象
     * @param isShort 是否短时间显示（false则为长时间显示）
     */
    public static void showToast(Context context, int resId, boolean isShort) {
        if (isShort) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 直接显示Toast
     * 
     * @param context 当前环境上下文对象
     * @param resId 字符串资源id
     */
    public static void showToast(Context context, int resId) {
        showToast(context, resId, true);
    }

    /**
     * 直接显示Toast
     * 
     * @param context 当前环境上下文对象
     * @param text 内容
     */
    public static void showToast(Context context, String text) {
        showToast(context, text, true);
    }

    /**
     * 关闭输入法
     */
    public static void closeEditer(Activity context) {
        View view = context.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager)context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断网络是否可用
     */
    public static boolean CheckNetworkConnection(Context context) {
        ConnectivityManager con = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // 当前网络不可用
            return false;
        }
        return true;
    }

    /**
     * 判断wifi网络是否可用
     */
    public static boolean IsWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * Get the external app cache directory.
     * 
     * @param context The context to use
     * @return The external cache dir
     */
    @TargetApi(8)
    public static File getExternalFileDir(Context context) {
        if (hasFroyo()) {
            final File file = context.getExternalFilesDir(null);
            return file;
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String fileDir = "/Android/data/" + context.getPackageName() + "/files";
        return new File(Environment.getExternalStorageDirectory().getPath() + fileDir);
    }

    public static File getCacheDir(Context context) {
        File cacheDir = null;
        try {
            cacheDir = getExternalFileDir(context);
        } catch (Exception e) {
            e.printStackTrace();
            cacheDir = context.getFilesDir();
        }
        return cacheDir;
    }

    /**
     * >= android 2.2 版本
     * 
     * @return
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * >= android 2.3 版本
     * 
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * >= android 3.0 版本
     * 
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * 收起输入法
     * 
     * @param ctx
     * @param view
     */
    public static void HideKeyboard(Context ctx, View view) {
        if (null == view)
            return;
        InputMethodManager imm = (InputMethodManager)ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 字符串转Html
     * 
     * @param text 字符串
     * @return 格式化的html。
     */
    public static Spanned formatHtml(String text) {
        return Html.fromHtml(text);
    }

}
