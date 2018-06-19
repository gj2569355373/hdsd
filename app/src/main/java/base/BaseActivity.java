package base;

import android.app.ProgressDialog;

import android.content.Intent;

import android.content.pm.ActivityInfo;

import android.databinding.ViewDataBinding;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bugtags.library.Bugtags;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.request.RequestOptions;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.tool.DisplayUtil;
import com.zchd.library.WebViewUtil.LogUtils;
import com.zchd.library.base.ActivityStack;
import com.zchd.library.network.http.NetworkActivity;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by GJ on 2016/9/18.
 */
public abstract class BaseActivity<C> extends NetworkActivity {
    protected ActivityStack mActivityStack;
    protected C Conmponent;
    private boolean Screen_lock = true;
    private ProgressDialog dialog = null;
    private String dialogtextBD = "";
    private boolean dialogB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getKey() != null) {
            if (savedInstanceState != null) {
                Intent intent = new Intent();
                for (int x = 0; x < getKey().length; x++)
                    intent.putExtra(getKey()[x], savedInstanceState.getString(getKey()[x]));
                setIntent(intent);
                Log.e("tag", getLocalClassName() + "数据恢复" + intent.toString());
                if (savedInstanceState.getBoolean("login")) {
                    HdsdApplication.id = savedInstanceState.getString("id");
                    HdsdApplication.token = savedInstanceState.getString("token");
                    HdsdApplication.avatar = savedInstanceState.getString("avatar");
                    HdsdApplication.nickname = savedInstanceState.getString("nickname");
                    HdsdApplication.hasUnRead = savedInstanceState.getString("hasUnRead");
                    HdsdApplication.weixinName = savedInstanceState.getString("weixinName");
                    HdsdApplication.qqName = savedInstanceState.getString("qqName");
                    HdsdApplication.weiboName = savedInstanceState.getString("weiboName");
                    HdsdApplication.mobile = savedInstanceState.getString("mobile");
                    HdsdApplication.login = savedInstanceState.getBoolean("login");
                }
            }
        }
        activityAnim();
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        setDataBinding(null);
        setComponent(getActivityComponent());
        //竖屏锁定
        if (Screen_lock)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivityStack = ActivityStack.create();
        mActivityStack.addActivity(this);
        // 初始化PreferenceUtil

//        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#37363a"), false);//设置状态栏颜色

//        PushAgent.getInstance(this).onAppStart();//如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效
//        StatusBarCompat.translucentStatusBar(this);
//        StatusBarCompat.setStatusBarColor(this,  Color.parseColor("#F7F8F9"));
        if (getStyleColor())
            com.githang.statusbar.StatusBarCompat.setStatusBarColor(this, Color.parseColor("#F7F8F9"), true);//设置状态栏颜色
    }

    protected abstract void setComponent(ActivityComponent activityComponent);

    protected abstract void setDataBinding(ViewDataBinding dataBinding);

    public ActivityComponent getActivityComponent() {
        return HdsdApplication.get(this).getActivityComponent();
    }

    protected boolean getStyleColor() {
        return true;
    }

    public void activityAnim() {

    }

    public void setScreen_lock(boolean screen_lock) {
        Screen_lock = screen_lock;
    }

    protected abstract int getLayoutResID();

    public void showToast(String text) {
        Toasty.normal(this, text).show();
    }

    public void startActivitys(Class tClass) {
        startActivity(new Intent(this, tClass));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && dialogB) {
            LogUtils.e("tag", "onWindowFocusChanged--showProgressDialog");
            showProgressDialog(dialogtextBD);

        }
    }

    /**
     * LENGTH_SHORT
     * 普通Toast
     */
    public void showShortToast(String message) {
        Toasty.normal(this, message).show();
    }

    /**
     * LENGTH_LONG
     * 普通Toast
     */
    public void showLongToast(String message) {
        Toasty.normal(this, message).show();
    }

    public void showProgressDialog(String dialogtext) {
        if (dialog != null) {
            LogUtils.e("tag", "showProgressDialog-onWindowFocusChanged--1");
            setProgressDialog(dialogtext);
        } else {
            if (hasWindowFocus()) {
                LogUtils.e("tag", "showProgressDialog-onWindowFocusChanged--2");
                dialog = ProgressDialog.show(this, "", dialogtext, false, false);
                dialogB = false;
            } else {
                LogUtils.e("tag", "showProgressDialog-onWindowFocusChanged--3");
                dialogB = true;
                dialogtextBD = dialogtext;
            }
        }
    }

    public void dimssProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
            LogUtils.e("tag", "dimssProgressDialog-onWindowFocusChanged--1");
        } else {
            dialogB = false;
            dialogtextBD = "";
            LogUtils.e("tag", "dimssProgressDialog-onWindowFocusChanged--2");
        }
    }

    public void setProgressDialog(String message) {
        if (dialog != null)
            dialog.setMessage(message);
    }

    public void GlideRound(int path, ImageView imageView) {
        Glide.with(this).load(path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    /*
       * 加载圆形图片
       *
       * */
    public void GlideRound(String path, ImageView imageView) {
        Glide.with(this).load(path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    public void GlideRound(String path, ImageView imageView, int err) {
        Glide.with(this).load((path == "" || path == null) ? err : path)
                .apply(new RequestOptions().circleCrop())
                .into(imageView);
    }

    public void GlideRoundDP(String path, ImageView imageView, int dp) {
        MultiTransformation multi = new MultiTransformation(
                new RoundedCornersTransformation(DisplayUtil.dip2px(this, dp), 0, RoundedCornersTransformation.CornerType.ALL));
        Glide.with(this).load(path)
                .apply(new RequestOptions().centerCrop().bitmapTransform(multi))
                .into(imageView);
    }

    public void GlideGif(int path, ImageView imageView) {
        GlideApp.with(this).asGif().load(path).into(imageView);
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

    public boolean isDialog() {
        return dialog == null;
    }

    /*
    *
    *  保存数据的字段
    * */
    public String[] getKey() {
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (getKey() != null) {
            for (int x = 0; x < getKey().length; x++)
                outState.putString(getKey()[x], getIntent().getStringExtra(getKey()[x]) == null ? "0" : getIntent().getStringExtra(getKey()[x]));
            Log.e("tag", getLocalClassName() + "保存状态" + outState.toString());
        }
        if (HdsdApplication.login) {
            outState.putString("user_id", HdsdApplication.id);
            outState.putString("token", HdsdApplication.token);
            outState.putString("avatar", HdsdApplication.avatar);
            outState.putString("nickname", HdsdApplication.nickname);
            outState.putString("hasUnRead", HdsdApplication.hasUnRead);
            outState.putString("weixinName", HdsdApplication.weixinName);
            outState.putString("qqName", HdsdApplication.qqName);
            outState.putString("weiboName", HdsdApplication.weiboName);
            outState.putString("mobile", HdsdApplication.mobile);
            outState.putBoolean("login", HdsdApplication.login);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {//打开已存在的时候调用，试用android:launchMode="singleTask"
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onDestroy() {
        icssOkhttp.clear(this);
        super.onDestroy();
    }
}
