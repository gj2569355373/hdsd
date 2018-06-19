package com.zchd.hdsd.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.sqllites.SharedPreferences_operate;



/*
* 支付回调
* */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private SharedPreferences_operate operate;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operate=new SharedPreferences_operate( User.hdsd, HdsdApplication.getContext());
        api = WXAPIFactory.createWXAPI(this, Constants.WEIXINAPPID);
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