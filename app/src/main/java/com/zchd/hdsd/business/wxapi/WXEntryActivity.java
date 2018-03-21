package com.zchd.hdsd.business.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import webicss.webicss.R;
import webicss.webicss.VcodeApplication;
import webicss.webicss.model.OtherAPPID;

/**
 * Created by GJ on 2017/1/4.
 * 分享回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_entry_activity);
        Log.e("tag","WXEntryActivityonCreate");
        api = WXAPIFactory.createWXAPI(VcodeApplication.getContext(), OtherAPPID.WEI_XIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("tag","onResp");//分享回调跟登陆回调的类是一样的,根据BaseResp的类型来区分是登陆还是分享。
//        if (baseResp.getType())
        switch (baseResp.errCode) {
            case 0:
//                Toast.makeText(this.getApplicationContext(), "付款成功", Toast.LENGTH_SHORT).show();
                break;

            case -1:
//                Toast.makeText(this.getApplicationContext(), "付款失败", Toast.LENGTH_SHORT).show();
                break;

            case -2://无需处理。发生场景：用户不支付了，点击取消，返回APP。
//                Toast.makeText(this.getApplicationContext(), "取消支付", Toast.LENGTH_SHORT).show();
                break;
        }
       this.finish();
    }
}
