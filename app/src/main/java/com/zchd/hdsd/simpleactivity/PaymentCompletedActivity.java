package com.zchd.hdsd.simpleactivity;


import android.databinding.ViewDataBinding;
import android.widget.TextView;

import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;

import base.BaseActivity;

/**
 * Created by GJ on 2016/7/21.
 * 完成付款
 */
public class PaymentCompletedActivity extends BaseActivity{
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        findViewById(R.id.back).setOnClickListener(arg0 -> finish());
        ((TextView) findViewById(R.id.title)).setText(gettitle());
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.paymentcompleted;
    }

    protected String gettitle() {
        return "完成付款";
    }
}
