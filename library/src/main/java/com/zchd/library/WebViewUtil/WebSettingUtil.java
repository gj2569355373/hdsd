package com.zchd.library.WebViewUtil;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;

import com.zchd.library.network.IsNetwork;


/**
 * Created by GJ on 2017/3/24.
 */
public class WebSettingUtil {
    public void setWebSetting(WebSettings settings, Application application){
        settings.setUseWideViewPort(true);//设置此属性,可任意比例缩放
        settings.setBuiltInZoomControls(true);//设定支持控制图标
        settings.setSupportZoom(true);//设定支持缩放
        settings.setDisplayZoomControls(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染优先级
        settings.setDomStorageEnabled(true);// 设置可以使用localStorage
        settings.setDatabaseEnabled(true); // 应用可以有数据库
        String dbPath = application.getDir("database", Context.MODE_PRIVATE).getPath();
        Log.e("tag", "DB" + dbPath);
        settings.setDatabasePath(dbPath);//设置数据库路径
        settings.setLoadWithOverviewMode(true); //自适应屏幕
        settings.setJavaScriptEnabled(true);//开启支持JavaScript
        settings.setAppCacheEnabled(true);  // 应用可以有缓存
        //解决腾讯视频等无法播放问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String appCaceDir = application.getDir("webcache", Context.MODE_PRIVATE).getPath();
        Log.e("tag", "appCace" + appCaceDir);
        settings.setAppCachePath(appCaceDir);
        //优先使用缓存，getCacheDir()可获取缓存目录；从而设置清空项
        if (IsNetwork.isNetworkAvailable(application))
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        else
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
    }
}
