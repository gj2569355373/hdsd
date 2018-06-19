package com.zchd.hdsd.business.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.camera.CameraActivity;
import com.zchd.hdsd.business.camera.CapturesActivity;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.mall.SeachActivity;
import com.zchd.hdsd.simpleactivity.LoginActivity;
import com.zchd.hdsd.simpleactivity.NewsListActivity;
import com.zchd.hdsd.tool.UpdateVersionTool;
import com.zchd.hdsd.view.TableMainAddView;
import com.zchd.library.WebViewUtil.LogUtils;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.widget.IPopupWindow;
import com.zchd.library.widget.PromptPopwindow;

import base.BaseFragment;
import base.BaseFragmentActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GJ on 2018/3/21.
 */
public class MainActivity extends BaseFragmentActivity {
    TableMainAddView tableMainAddView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.main_title_bg)
    ImageView main_title_bg_search;
    @BindView(R.id.main_title_bgs)
    ImageView main_title_bgs;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    @BindView(R.id.main_table_lin)
    RelativeLayout mainTableLin;
    public static boolean Refresh = false;
    public static boolean Refresh_GWC = true;
    public static boolean Refresh_KC = true;
    @BindView(R.id.news_yd)
    View news_yd;
    @BindView(R.id.have_title_rlayout)
    RelativeLayout haveTitleRlayout;
    @BindView(R.id.title_scanning_llayout)
    LinearLayout titleScanningLlayout;
    @BindView(R.id.title_news_image)
    ImageView titleNewsImage;
    @BindView(R.id.title_search_llayout)
    LinearLayout titleSearchLlayout;
    @BindView(R.id.have_search_rlayout)
    RelativeLayout haveSearchRlayout;
    @BindView(R.id.search_img)
    ImageView search_img;
    @BindView(R.id.search_text)
    TextView search_text;
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mFragmentTransaction = null;
    public String current = "";
    public String fragment1Tag = "fragment1", fragment2Tag = "fragment2", fragment3Tag = "fragment3", fragment4Tag = "fragment4";
    private long firstTime = 0;
    String XZ="#F7F8F9";
    String YESXZ="#B20000";
    SharedPreferences_operate operate;
    boolean isjiazai=false;
    private PopupWindow mPopupWindow;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        initData(1);
        operate =new SharedPreferences_operate("login",HdsdApplication.getInstance());
        if (operate.getint("version") != 0) {
            try {
                if (UpdateVersionTool.getVersionCode() < operate.getint("version")) {
                    //开启弹出框
                    isjiazai = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {//Activity加载完毕调用
        if (hasFocus && isjiazai) {
            showUpDate();
            isjiazai = false;
        }
        super.onWindowFocusChanged(hasFocus);
    }

    void showUpDate() {
        if (mPopupWindow == null) {
            View views = getLayoutInflater().inflate(R.layout.updata, null, false);
            mPopupWindow = new PopupWindow(views, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.AnimBottom);
            mPopupWindow.showAtLocation(mainTableLin, Gravity.CENTER, 0, 0);
            TextView updata_version = (TextView) views.findViewById(R.id.updata_title);
            updata_version.setText(getString(R.string.app_name));
            TextView updata_data = (TextView) views.findViewById(R.id.updata_data);
            updata_data.setMovementMethod(new ScrollingMovementMethod());
            try {
                updata_data.setText("当前版本：" + UpdateVersionTool.getVersionName() + "\n发现版本：" + operate.getString("revision") + "\n版本更新内容：\n" + operate.getString("versiondate"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView updata_down = (TextView) views.findViewById(R.id.dialog_ok);
            updata_down.setText("立即更新");
            views.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            });
            updata_down.setOnClickListener(view -> {
                UpdateVersionTool updateVersionTool = new UpdateVersionTool();
                updateVersionTool.UpdateVersions(MainActivity.this, operate.getString("url"), getString(R.string.app_name) + operate.getString("revision"));
                operate.addint("version", 0);
                mPopupWindow.dismiss();
            });
            views.findViewById(R.id.diglog_cancel).setOnClickListener(v -> {
                operate.addint("version", 0);
                mPopupWindow.dismiss();
            });
        }
        else
            mPopupWindow.showAtLocation(mainTableLin, Gravity.CENTER, 0, 0);
    }
    //改写物理按键返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            firstTime = secondTime;//更新firstTime
        } else {
            mActivityStack.appExit(this,false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("current",current);
        if (HdsdApplication.login){
            outState.putString("id",HdsdApplication.id);
            outState.putString("token",HdsdApplication.token);
            outState.putString("avatar",HdsdApplication.avatar);
            outState.putString("nickname",HdsdApplication.nickname);
            outState.putString("hasUnRead",HdsdApplication.hasUnRead);
            outState.putString("weixinName",HdsdApplication.weixinName);
            outState.putString("qqName",HdsdApplication.qqName);
            outState.putString("weiboName",HdsdApplication.weiboName);
            outState.putString("mobile",HdsdApplication.mobile);
            outState.putBoolean("login",HdsdApplication.login);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState!=null) {
            current = savedInstanceState.getString("current");
        }
        super.onCreate(savedInstanceState);
        if (current==""){
            setFragment1();
        }
        else
            setStyles();
    }

    private void setStyles(){
        if (current==fragment1Tag) {
            setFragment1();
            tableMainAddView.setIndexs(1);
        }
        else if (current==fragment2Tag) {
            setFragment2();
            tableMainAddView.setIndexs(2);
        }
        else if (current==fragment3Tag) {
            setFragment3();
            tableMainAddView.setIndexs(3);
        }
        else {
            setFragment4();
            tableMainAddView.setIndexs(4);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.main_activity;
    }

    public void setFragment1() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor(YESXZ),false);//设置状态栏颜色
        haveSearchRlayout.setVisibility(View.VISIBLE);
        GlideApp.with(this).load(R.drawable.header_img).into(main_title_bg_search);
//        StatusBarCompat.setStatusBarColor(Activity activity, int statusColor, int alpha)
        if (current == fragment1Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment1Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_sy();
            mFragmentTransaction.add(R.id.navigation_content, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();

    }

    public void setFragment4() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor(YESXZ));//设置状态栏颜色
        titleRightText.setVisibility(View.GONE);
        titleRightImage.setVisibility(View.GONE);
        title.setText(getString(R.string.wode));
        title.setTextColor(Color.parseColor("#ffffff"));
        haveSearchRlayout.setVisibility(View.GONE);
        GlideApp.with(this).load(R.drawable.header_img).into(main_title_bgs);
        if (current == fragment4Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment4Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_my();
            mFragmentTransaction.add(R.id.navigation_content, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();
    }

    public void setFragment3() {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor(XZ));//设置状态栏颜色
        haveSearchRlayout.setVisibility(View.GONE);
        titleRightText.setVisibility(View.VISIBLE);
        titleRightImage.setVisibility(View.GONE);
        title.setText(getString(R.string.gouwuche));
        title.setTextColor(Color.parseColor("#000000"));
        GlideApp.with(this).load("").into(main_title_bgs);
        if (current == fragment3Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment3Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_gwc();
            mFragmentTransaction.add(R.id.navigation_content, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();
    }
    private void setFragment2() {
        titleRightText.setVisibility(View.GONE);
        titleRightImage.setVisibility(View.VISIBLE);
        title.setText(getString(R.string.daiding));
        title.setTextColor(Color.parseColor("#000000"));
        StatusBarCompat.setStatusBarColor(this, Color.parseColor(XZ));//设置状态栏颜色
        haveSearchRlayout.setVisibility(View.GONE);
        GlideApp.with(this).load("").into(main_title_bgs);
        if (current == fragment2Tag)
            return;
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment ff = mFragmentManager.findFragmentByTag(current);
        if (ff != null)
            mFragmentTransaction.hide(ff);
        current = fragment2Tag;
        Fragment fragment = mFragmentManager.findFragmentByTag(current);
        if (fragment == null) {
            fragment = new Fragment_course();
            mFragmentTransaction.add(R.id.navigation_content, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();
    }

    private void initData(int xz) {
        tableMainAddView = new TableMainAddView(xz);
        mainTableLin.addView(tableMainAddView.addView(this));
        tableMainAddView.setIonclickListener(new TableMainAddView.IonclickListener() {
            @Override
            public void shouye(View v) {
                setFragment1();
            }

            @Override
            public void paihang(View v) {//我的课程
                if (!HdsdApplication.login){
                    showLogin();
                    return;
                }
                setFragment2();
            }

            @Override
            public void wode(View v) {
                setFragment4();
            }

            @Override
            public void gouwuche(View v) {
                if (!HdsdApplication.login){
                    showLogin();
                    return;
                }
                setFragment3();
            }

        });

        titleRightText.setText(getText(R.string.bianji));
    }



    @OnClick({R.id.title_right_lin,R.id.title_news_llayout,R.id.title_scanning_llayout,R.id.title_search_llayout})
    public void onViewClicked(View view) {
        if (!HdsdApplication.login){
            showLogin();
            return;
        }
        switch (view.getId()) {
            case R.id.title_right_lin:
                if (current == fragment2Tag) {//课程激活
                    Intent intent = new Intent(MainActivity.this, CapturesActivity.class);
                    startActivityForResult(intent, 109);
                }
                if (current == fragment3Tag) {//购物车
                    ((Fragment_gwc) getFragments(fragment3Tag)).onRightTextClick((titleRightText));
                }
                break;
            case R.id.title_news_llayout:
                startActivitys(NewsListActivity.class);
                //消息
                break;
            case R.id.title_scanning_llayout:
                showProgressDialog("");
                Intent intentr=new Intent(MainActivity.this, CameraActivity.class);
                intentr.putExtra("type","1");
                startActivityForResult(intentr,12);
                //扫一扫
                break;
            case R.id.title_search_llayout:
                //搜索
                startSeachActivity();
                break;
        }
    }
    @TargetApi(21)
    private void startSeachActivity(){
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this
                    , Pair.create(search_img, "myt3"));
            Intent intent = new Intent(MainActivity.this, SeachActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent, activityOptions.toBundle());
            overridePendingTransition(R.anim.activity_noanim_in, R.anim.activity_noanim_out);
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, SeachActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
        }
    }

    public void showLogin() {
        new PromptPopwindow().showAlertDialog(MainActivity.this, mainTableLin, "是否开始登录？", "是", "否", new IPopupWindow() {
            @Override
            public void cancel() {
            }

            @Override
            public void Oks() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }

    public BaseFragment getFragments(String TAG) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        // 开启Fragment事务
        return (BaseFragment) mFragmentManager.findFragmentByTag(TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {//个人信息更新
            getFragments(fragment4Tag).onActivityResult(requestCode, resultCode, data);
        }
        else if (requestCode == 109 ) {//个人信息更新
            getFragments(fragment2Tag).onActivityResult(requestCode, resultCode, data);
        }
        else if (requestCode==1000){
            getFragments(fragment3Tag).onActivityResult(requestCode, resultCode, data);
        }
        else if (requestCode==12){
            dimssProgressDialog();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Refresh = false;
        Refresh_GWC = true;
        Refresh_KC = true;
        if(intent.getStringExtra("type").equals("1")){
            setFragment1();
            tableMainAddView.setIndexs(1);
        }
        else if (intent.getStringExtra("type").equals("3")){
            setFragment3();
            titleRightText.setVisibility(View.VISIBLE);
            titleRightImage.setVisibility(View.GONE);
            title.setText(getString(R.string.gouwuche));
            title.setTextColor(Color.parseColor("#000000"));
            tableMainAddView.setIndexs(3);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        news_yd.setVisibility(HdsdApplication.hasUnRead.equals("1")?View.VISIBLE:View.GONE);
    }
}
