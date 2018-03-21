package com.zchd.library.network.http;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;


/**
 * Created by GJ on 2016/9/18.
 */
public class NetworkActivity extends Activity {
    public IcssOkhttp icssOkhttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        icssOkhttp=new IcssOkhttp();
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }

    @Override
    protected void onDestroy() {
        icssOkhttp.clear(this);
        icssOkhttp=null;
        super.onDestroy();
    }


    /**
     *LENGTH_SHORT
     * 普通Toast
     */
    public void showShortToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     *LENGTH_LONG
     * 普通Toast
     */
    public void showLongToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
