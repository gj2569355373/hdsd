package com.zchd.hdsd.wxapi;

import android.util.Log;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GJ on 2017/1/4.
 * 微信支付
 */
public class WxPayUtil {
    private IWXAPI api;
    private PayReq mPayReq;//微信支付
    private boolean B;
    //在调用支付功能所必须的   建议Activity onCreat()执行
    public void regToWx(){
        api = WXAPIFactory.createWXAPI(HdsdApplication.getContext(),null);
        B= api.registerApp(Constants.WEIXINAPPID);//将应用的APPID注册到微信
        mPayReq=new PayReq();
        Log.e("tag","WX注册是否成功="+B);
    }
    /*
    * 调起微信支付
    * */
    public void pay(JSONObject jsonObject){
        Log.e("tag","WX注册是否成功="+B);
        try {
            mPayReq.appId = jsonObject.getString("appid");//商户在微信开放平台申请的应用id
            mPayReq.partnerId = jsonObject.getString("partnerid");//商户id
            mPayReq.prepayId = jsonObject.getString("prepayid");//预支付订单
            mPayReq.packageValue = jsonObject.getString("package");//商家根据文档填写的数据和签名
            mPayReq.nonceStr = jsonObject.getString("noncestr");//随机串，防重发
            mPayReq.timeStamp = jsonObject.getString("timestamp");//时间戳，防重发
            mPayReq.sign = jsonObject.getString("sign");//商家根据微信开放平台文档对数据做的签名
        } catch (JSONException e) {
            Log.e("tag","支付Json解析失败");
            e.printStackTrace();
        }
        api.sendReq(mPayReq);//真正调起微信支付
    }
}
