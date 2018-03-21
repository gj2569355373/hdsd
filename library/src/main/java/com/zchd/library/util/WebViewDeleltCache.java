package com.zchd.library.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by GJ on 2016/12/20.
 */
public class WebViewDeleltCache {
    public static String TAG="tag";
    /**
     * 清除WebView缓存
     */
    public static void clearWebViewCache(Application application){

        //清理Webview缓存数据库
        File DBCacheDir = new File(application.getDir("database", Context.MODE_PRIVATE).getPath());
        Log.e(TAG, "DBCacheDir path="+DBCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(application.getDir("webcache", Context.MODE_PRIVATE).getPath());
        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(DBCacheDir.exists()){
            deleteFile(DBCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

    public static long getCache(Application application){
        int size=0;
        File DBCacheDir = new File(application.getDir("database", Context.MODE_PRIVATE).getPath());
        Log.e(TAG, "DBCacheDir path="+DBCacheDir.getAbsolutePath());
        File webviewCacheDir = new File(application.getDir("webcache", Context.MODE_PRIVATE).getPath());
        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        try {
            if(DBCacheDir.exists()) {
                size += getFolderSize(DBCacheDir);
            }
            Log.e(TAG, "DBCacheDir-size="+size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(webviewCacheDir.exists()) {
                size += getFolderSize(webviewCacheDir);
            }
            Log.e(TAG, "webviewCacheDir-size="+size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    /*
    * 获取文件大小
    * */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}

