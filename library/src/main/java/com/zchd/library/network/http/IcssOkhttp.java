package com.zchd.library.network.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.zchd.library.network.linstener.GosnListener;
import com.zchd.library.network.linstener.IcssCallback;
import com.zchd.library.network.linstener.JsonLinstener;
import com.zchd.library.network.linstener.TextLinstener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import okhttp3.Call;


/**
 * Created by GJ on 2016/9/18.
 */
public class IcssOkhttp {
    /**
     * 无弹出框
     */
    public void HttppostString(String url, TextLinstener listener, final Map<String, String> map, Activity activity){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(activity)//Activity销毁则取消网络
                .build()
                .execute(listener);
    }
    public void HttpGetString(String url, StringCallback listener){
        Log.e("url", url.toString());
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(listener);
    }
    /**
     * 无弹出框不与Activity绑定
     */
    public void HttppostString(String url, TextLinstener listener, final Map<String, String> map){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(listener);
    }
    /**
     * 无弹出框
     */
    public void HttppostString(String url, TextLinstener listener, final Map<String, String> map, Fragment activity){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(activity)//Activity销毁则取消网络
                .build()
                .execute(listener);
    }
    /**
     * 有弹出框
     */
    public void HttppostString(String url, TextLinstener listener, final Map<String, String> map, Fragment activity, String dialogtext){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        if (dialogtext!=null) {
            ProgressDialog dialog = ProgressDialog.show(activity.getContext(), "", dialogtext, false, false);
            listener.setDialog(dialog);
        }
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(activity)//Activity销毁则取消网络
                .build()
                .execute(listener);
    }
    /**
     * 有弹出框
     */
    public void HttppostString(String url, TextLinstener listener, final Map<String, String> map, Activity activity, String dialogtext){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        if (dialogtext!=null) {
            ProgressDialog dialog = ProgressDialog.show(activity, "", dialogtext, false, false);
            listener.setDialog(dialog);
        }
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(activity)//Activity销毁则取消网络
                .build()
                .execute(listener);
    }
    public void clear(Activity act){
        OkHttpUtils.getInstance().cancelTag(act);
    }
    public void clear(Fragment fragment){
        OkHttpUtils.getInstance().cancelTag(fragment);
    }
    /**
     * 未与Activity绑定
     */
    public void okhttppost(String url, Map<String,String> map, IcssCallback callback){
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(callback);
    }
    /**
     * Gson解析
     */
    public void okhttppostgosn(String url, Map<String,String> map, GosnListener listener, Activity activity, String dialogtext){
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        ProgressDialog dialog = ProgressDialog.show(activity, "",dialogtext,false,false);
        listener.setDialog(dialog);
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(listener);
    }
    /**
     * Json解析
     */
    public void HttppostJson(String url, Map<String,String> map, Activity activity, String dialogtext, JsonLinstener listener){
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        ProgressDialog dialog = ProgressDialog.show(activity, "",dialogtext,false,false);
        listener.setDialog(dialog);
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(listener);
    }
    /*
    * 解决参数为空的异常问题
    * */
    private void mapNoNull(Map map)
    {
        List<String> list=null;
        Set entries = map.entrySet( );
        if(entries != null) {
            Iterator iterator = entries.iterator( );
            while(iterator.hasNext( )) {
                Map.Entry entry = (Map.Entry) iterator.next( );
                String key = (String) entry.getKey( );
                String value =(String) entry.getValue();
                if (value==null)
                {
                    if (list==null)
                        list= new ArrayList<>();
                    list.add(key);
                }
            }
        }
        if (list!=null)
            for (int i=0;i<list.size();i++)
                map.put(list.get(i),"");
    }
    /*
    * 文件下载
    * */
    public void httpfile(String url, String filepath, String filename, final DownFile downFile){
        OkHttpUtils//
                .get()//
                .url(url)//http://dldir1.qq.com/weixin/android/weixin657android1040.apk//http://api.nohttp.net/download/4.apk
                .build()//
                .execute(new FileCallBack(filepath, filename)//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("tag", "onError :" + e.getMessage());
                        downFile.onError(call,e,id);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e("tag", "onResponse :" + response.getAbsolutePath());
                        downFile.onResponse(response,id);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
//                        textView.setText("下载中\n"+ (int)(100 * progress) +"%");
                        downFile.inProgress(progress,total,id);
                    }
                });
    }
    public interface DownFile{
        public void onResponse(File response, int id);
        public void onError(Call call, Exception e, int id);
        public void inProgress(float progress, long total, int id);
    }
    /*
    * 文件上传
    * */
    public void postfile(String url, Map<String ,String> map, File file, String key, String name, Callback fileCallBack){
        OkHttpUtils.post()//
                .addFile(key,name, file)//
                .url(url)
                .params(map)//
                .build()//
                .execute(fileCallBack);
    }
    /**
     * 有弹出框,有证书
     */
    public void HttppostStringSH(String url, TextLinstener listener, final Map<String, String> map, Activity activity, String dialogtext){
        mapNoNull(map);
        Log.e("url", url.toString());
        Log.e("tag", map.toString());
        if (dialogtext!=null) {
            ProgressDialog dialog = ProgressDialog.show(activity, "", dialogtext, false, false);
            listener.setDialog(dialog);
        }
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .tag(activity)//Activity销毁则取消网络
                .build()
                .execute(listener);
    }
}
