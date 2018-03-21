package com.zchd.library.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by GJ on 2017/1/12.
 */
public class SmsUtil {
    public void sendses(Activity activity ,String message,String phone){
        Intent sendIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("smsto:"+phone));
        sendIntent.putExtra("sms_body", message);
        sendIntent.setType("vnd.android-dir/mms-sms");
        activity. startActivity(sendIntent);
    }
}
