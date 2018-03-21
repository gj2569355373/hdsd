package com.zchd.library.network.linstener;

import android.app.ProgressDialog;
import android.util.Log;

import com.zchd.library.base.ActivityStack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by GJ on 2016/9/18.
 */
public abstract class TextLinstener extends StringCallback {
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
        try {
            JSONObject jsonObject =new JSONObject(response);
            Log.e("tag",response.toString());
            if (jsonObject.getString("code").equals("-9"))
            {
                ActivityStack activityStack=ActivityStack.create();
                activityStack.appExit(null);
                Responses(response,id);
            }

            else {
                ActivityStack activityStack=ActivityStack.create();
//                activityStack.seterrorcode();
                Responses(response,id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errorhtml(response);
            Log.e("tag",response.toString());
            dialogdimss();
        }
    }
    public abstract void Errors(Call call, Exception e, int id);
    public abstract void Responses(String text , int id);
    public void errorhtml(String html){
    }

    public void setDialog(ProgressDialog dialog) {
        this.dialog = dialog;
    }
    private void dialogdimss(){
        if (dialog!=null)
            dialog.dismiss();
    }
}
