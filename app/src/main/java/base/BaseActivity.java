package base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.library.WebViewUtil.LogUtils;
import com.zchd.library.base.ActivityStack;
import com.zchd.library.network.http.NetworkActivity;
import com.zchd.library.widget.CircleTransform;


import java.util.Locale;

import butterknife.ButterKnife;


/**
 * Created by GJ on 2016/9/18.
 */
public abstract class BaseActivity<C> extends NetworkActivity {
    protected ActivityStack mActivityStack;
    protected C Conmponent;
    private boolean Screen_lock=true;
    private ProgressDialog dialog=null;
    private Bundle mKeyBundle=null;//记录Activity交互的的信息
    private String dialogtextBD="";
    private boolean dialogB=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getKey()!=null) {
            if (savedInstanceState == null) {
                for (int x=0;x<getKey().length;x++)
                    getmKeyBundle().putString(getKey()[x],getIntent().getStringExtra(getKey()[x]));
                Log.e("tag",getLocalClassName()+"第一次获取"+getmKeyBundle().toString());
            }
            else
            {
                for (int x=0;x<getKey().length;x++)
                    getmKeyBundle().putString(getKey()[x],savedInstanceState.getString(getKey()[x]));
                Log.e("tag",getLocalClassName()+"数据恢复"+getmKeyBundle().toString());
            }
        }
        activityAnim();
        ViewDataBinding dataBinding= DataBindingUtil.setContentView(
                this, getLayoutResID());
        setDataBinding(dataBinding);
//        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        setComponent(getActivityComponent());
        //竖屏锁定
        if (Screen_lock)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivityStack = ActivityStack.create();
        mActivityStack.addActivity(this);
        // 初始化PreferenceUtil

//        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#37363a"), false);//设置状态栏颜色

//        PushAgent.getInstance(this).onAppStart();//如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效
    }
    protected abstract void setComponent(ActivityComponent activityComponent);
    protected abstract void setDataBinding(ViewDataBinding dataBinding);
    public ActivityComponent getActivityComponent(){
        return  HdsdApplication.get(this).getActivityComponent();
    }

    public Bundle getmKeyBundle() {
        return mKeyBundle==null?mKeyBundle=new Bundle():mKeyBundle;
    }

    public void activityAnim(){

    }

    public void setScreen_lock(boolean screen_lock) {
        Screen_lock = screen_lock;
    }

    protected abstract int getLayoutResID();
    public void showToast(String text)
    {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }
    public void startActivitys(Class tClass){
        startActivity(new Intent(this, tClass));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus&&dialogB)
        {
            LogUtils.e("tag","onWindowFocusChanged--showProgressDialog");
           showProgressDialog(dialogtextBD);

        }
    }

    public void showProgressDialog(String dialogtext){
        if (dialog!=null) {
            LogUtils.e("tag","showProgressDialog-onWindowFocusChanged--1");
            setProgressDialog(dialogtext);
        }
        else {
            if (hasWindowFocus()) {
                LogUtils.e("tag","showProgressDialog-onWindowFocusChanged--2");
                dialog = ProgressDialog.show(this, "", dialogtext, false, false);
                dialogB=false;
            }
            else {
                LogUtils.e("tag","showProgressDialog-onWindowFocusChanged--3");
                dialogB=true;
                dialogtextBD=dialogtext;
            }
        }
    }
    public void dimssProgressDialog(){
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
            LogUtils.e("tag","dimssProgressDialog-onWindowFocusChanged--1");
        }
        else {
            dialogB=false;
            dialogtextBD="";
            LogUtils.e("tag","dimssProgressDialog-onWindowFocusChanged--2");
        }
    }
    public void setProgressDialog(String message){
        if (dialog!=null)
            dialog.setMessage(message);
    }
    /*
    * 加载圆形图片
    *
    * */
    public void GlideRound(String path, ImageView imageView)
    {
        Glide.with(this).load(path)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .crossFade()
                .into(imageView);
    }
    public void GlideRound(int path, ImageView imageView)
    {
        Glide.with(this).load(path)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .crossFade()
                .into(imageView);
    }
    /*
       * 加载圆形图片
       *
       * */
    public void GlideRound(String path, ImageView imageView, int erroriamge)
    {
        Glide.with(this).load(path)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleTransform(this))
                .error(erroriamge)
                .crossFade()
                .into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
//        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Bugtags.onDispatchTouchEvent(this, ev);
        return super.dispatchTouchEvent(ev);
    }
    public boolean isDialog(){
        return dialog==null;
    }

    /*
    *
    *  保存数据的字段
    * */
    public String[] getKey(){
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (getKey()!=null)
        {
            for (int x=0;x<getKey().length;x++)
                outState.putString(getKey()[x],getmKeyBundle().getString(getKey()[x]));
            Log.e("tag",getLocalClassName()+"保存状态"+outState.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {//打开已存在的时候调用，试用android:launchMode="singleTask"
        super.onNewIntent(intent);
        setIntent(intent);
        if (getKey()!=null) {
            for (int x = 0; x < getKey().length; x++)
                getmKeyBundle().putString(getKey()[x], getIntent().getStringExtra(getKey()[x]));
            Log.e("tag", getLocalClassName() + "onNewIntent获取" + getmKeyBundle().toString());
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }



}
