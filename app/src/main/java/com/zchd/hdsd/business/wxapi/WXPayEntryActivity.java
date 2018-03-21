package com.zchd.hdsd.business.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import icssmvp.icsslibrary.sqllites.SharedPreferences_operate;
import webicss.webicss.VcodeApplication;
import webicss.webicss.model.OtherAPPID;
import webicss.webicss.model.User;

/*
* 支付回调
* */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private SharedPreferences_operate operate;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operate=new SharedPreferences_operate( User.vcode, VcodeApplication.getContext());
        api = WXAPIFactory.createWXAPI(this, OtherAPPID.WEI_XIN_PAY_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    operate.addString("wxpaycode","1");
                    this.finish();
                break;

                case -1://可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                    operate.addString("wxpaycode","-1");
                    this.finish();
//                    Toast.makeText(this.getApplicationContext(), "付款失败" + resp.errStr, Toast.LENGTH_LONG).show();
                    break;

                case -2://无需处理。发生场景：用户不支付了，点击取消，返回APP。
                    operate.addString("wxpaycode","-2");
                    this.finish();
                    break;
            }
        }
    }
}