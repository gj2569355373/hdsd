package com.zchd.library.network.linstener;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by GJ on 2016/9/18.
 */
public abstract class GosnListener<T> extends StringCallback {
    private ProgressDialog dialog=null;
    @Override
    public void onError(Call call, Exception e, int id) {
        try {
            Log.e("tag", e.toString());
            dialogdimss();
            Errors(call, e, id);
        }catch (Exception el){
            el.printStackTrace();
            Log.e("tag","try----"+el.toString());
            return;
        }
    }

    @Override
    public void onResponse(String response, int id) {
        Log.e("tag",response.toString());
        dialogdimss();
        Boolean is=true;
        T t=null;
        try {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<T>(){}.getType();
        t=gson.fromJson(response, type);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e("tag","Gson转换失败"+e.toString());
            is=false;
        }
        if (is)
            Responses(t,id);
    }
    public abstract void Errors(Call call, Exception e, int id);
    public abstract void Responses(T t, int id);
    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    private void dialogdimss(){
        if (dialog!=null)
            dialog.dismiss();
    }
}