package com.zchd.library.network.linstener;

import android.app.ProgressDialog;
import android.util.Log;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by GJ on 2016/9/21.
 */
public abstract class JsonLinstener extends StringCallback {

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
        try {
            JSONObject json=new JSONObject(response);
            Responses(json,id);
        } catch (JSONException e) {
            e.printStackTrace();
            Errors(null,null,0);
        }
        dialogdimss();
    }
    public abstract void Errors(Call call, Exception e, int id);
    public abstract void Responses(JSONObject json , int id);
    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    private void dialogdimss(){
        if (dialog!=null)
            dialog.dismiss();
    }
}
