package com.zchd.library.network.linstener;

import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by GJ on 2016/9/18.
 */
public abstract class IcssCallback extends StringCallback {
    @Override
    public void onError(Call call, Exception e, int id) {
        try {
            Log.e("tag", e.toString());
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
        Responses(response,id);
    }
    public abstract void Errors(Call call, Exception e, int id);
    public abstract void Responses(String text, int id);
}
