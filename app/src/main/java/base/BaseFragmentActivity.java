package base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.library.base.ActivityStack;
import com.zchd.library.network.http.NetworkFragmentActivity;
import com.zchd.library.widget.CircleTransform;


import java.util.Locale;

import butterknife.ButterKnife;


/**
 * Created by GJ on 2016/9/18.
 */
public abstract class BaseFragmentActivity<C> extends NetworkFragmentActivity {
    protected ActivityStack mActivityStack;
    protected C Conmponent;
    private ProgressDialog dialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding dataBinding= DataBindingUtil.setContentView(
                this, getLayoutResID());
        setDataBinding(dataBinding);
//        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        setComponent(getActivityComponent());
        //竖屏锁定
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
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
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


    public ActivityStack getmActivityStack() {
        return mActivityStack;
    }

    protected abstract int getLayoutResID();
    public void showToast(String text)
    {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }
    public void startActivitys(Class tClass){
        startActivity(new Intent(this, tClass));
    }
    public void showProgressDialog(String dialogtext){
        if (dialog!=null) {
            setProgressDialog(dialogtext);
        }
        else {
            if (hasWindowFocus()) {
                dialog = ProgressDialog.show(this, "", dialogtext, false, false);
            }
        }
    }
    public void dimssProgressDialog(){
        if (dialog!=null) {
            dialog.dismiss();
            dialog=null;
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


    public boolean isDialog(){
        return dialog==null;
    }


}
