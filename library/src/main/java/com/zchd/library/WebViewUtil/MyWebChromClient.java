package com.zchd.library.WebViewUtil;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.zchd.library.WebViewUtil.webvideo.IVideo;

import java.lang.ref.WeakReference;



/**
 * Created by GJ on 2017/6/15.
 */
abstract public class MyWebChromClient extends WebChromeClient {
    private WebFiletakeUtil webFiletakeUtil;
    private IVideo mIVideo;
    private WeakReference<Activity> mActivityWeakReference = null;

    public MyWebChromClient(Activity activity, IVideo mIVideo) {
        this.mIVideo = mIVideo;
        mActivityWeakReference = new WeakReference<Activity>(activity);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {  // For Android >= 5.0
        getWebFiletakeUtil(). setmUploadCallbackAboveL(filePathCallback);
        getWebFiletakeUtil().take(getmActivityWeakReference().get());
        return true;

    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType, String capture) {  //For Android  >= 4.1
        getWebFiletakeUtil().setmUploadMessage(uploadMsg);
        getWebFiletakeUtil().take(getmActivityWeakReference().get());
    }
    public WebFiletakeUtil getWebFiletakeUtil() {
        if (webFiletakeUtil==null)
            webFiletakeUtil=new WebFiletakeUtil();
        return webFiletakeUtil;
    }

    public WeakReference<Activity> getmActivityWeakReference() {
        return mActivityWeakReference;
    }
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Log.i("Info", "view:" + view + "   callback:" + callback);
        if(mIVideo!=null)
            mIVideo.onShowCustomView(view,callback);
    }
    @Override
    public void onHideCustomView() {
        LogUtils.i("Info","onHideCustomView --Videa:"+mIVideo);
        if(mIVideo!=null)
            mIVideo.onHideCustomView();
    }
    @Override
    public View getVideoLoadingProgressView() {
        if (getmActivityWeakReference().get()==null)
            return null;
        if (super.getVideoLoadingProgressView()==null)
        {
            FrameLayout frameLayout = new FrameLayout(getmActivityWeakReference().get());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }
        else
            return super.getVideoLoadingProgressView();
    }
}
