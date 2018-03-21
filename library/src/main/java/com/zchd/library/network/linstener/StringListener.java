package com.zchd.library.network.linstener;

import android.app.ProgressDialog;
import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by GJ on 2017/7/4.
 */
public abstract class StringListener extends StringCallback {
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
        dialogdimss();
        Log.e("tag",response.toString());
        Responses(response,id);
    }
    public abstract void Errors(Call call, Exception e, int id);
    public abstract void Responses(String text , int id);

    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    private void dialogdimss(){
        if (dialog!=null)
            dialog.dismiss();
    }
}
