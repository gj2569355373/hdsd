package com.zchd.hdsd.view;

import android.view.View;

import java.util.Calendar;

/**
 * Created by apple on 2018/5/18.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {


    public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
    private long lastClickTime = 0;

    protected abstract void onNoDoubleClick(View v);
    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
