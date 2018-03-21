package com.zchd.library.WebViewUtil.webvideo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.zchd.library.WebViewUtil.LogUtils;


/**
 * Created by GJ on 2017/6/15.
 */
public class VideoImpl implements IVideo, EventInterceptor {


    private Activity mActivity;
    private WebView mWebView;

    public VideoImpl(Activity mActivity, WebView webView) {
        this.mActivity = mActivity;
        this.mWebView = webView;

    }

    private View moiveView = null;
    private ViewGroup moiveParentView = null;
    private WebChromeClient.CustomViewCallback mCallback;
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

        LogUtils.i("Info", "onShowCustomView:" + view);

        Activity mActivity;
        if ((mActivity = this.mActivity) == null)
            return;
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (moiveView != null){
            callback.onCustomViewHidden();
            return;
        }

        if (mWebView != null)
            mWebView.setVisibility(View.GONE);

        if (moiveParentView == null) {
            FrameLayout mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            moiveParentView = new FrameLayout(mActivity);
            moiveParentView.setBackgroundColor(Color.BLACK);
            mDecorView.addView(moiveParentView);
        }
        this.mCallback=callback;
        moiveParentView.addView(this.moiveView = view);


        moiveParentView.setVisibility(View.VISIBLE);
        hideBottomUIMenu();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onHideCustomView() {

        LogUtils.i("Info", "onHideCustomView:" + moiveView);
        if (moiveView == null)
            return;
        if (mActivity!=null&&mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        moiveView.setVisibility(View.GONE);
        if (moiveParentView != null && moiveView != null) {
            moiveParentView.removeView(moiveView);

        }
        if (moiveParentView != null)
            moiveParentView.setVisibility(View.GONE);

        if(this.mCallback!=null)
            mCallback.onCustomViewHidden();
        this.moiveView = null;
        if (mWebView != null)
            mWebView.setVisibility(View.VISIBLE);
        showBottomUIMenu();
    }

    @Override
    public boolean isVideoState() {
        return moiveView != null;
    }

    @Override
    public boolean event() {

        LogUtils.i("Info", "event:" + isVideoState());
        if (isVideoState()) {
            onHideCustomView();
            return true;
        } else {
            return false;
        }
    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        Log.e("tag","隐藏全屏");
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = mActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = mActivity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    protected void showBottomUIMenu() {
        Log.e("tag","显示虚拟按键");
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = mActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = mActivity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
