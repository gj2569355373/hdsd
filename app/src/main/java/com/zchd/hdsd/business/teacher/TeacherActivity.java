package com.zchd.hdsd.business.teacher;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.view.AppBarStateChangeListener;
import com.zchd.hdsd.view.MainPagerAdapter;
import com.zchd.hdsd.view.TextPopupwindow;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import base.BaseFragmentActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/10.
 */
public class TeacherActivity extends BaseFragmentActivity {
    @BindView(R.id.mAppBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.teacher_br)
    ImageView teacherBr;
    @BindView(R.id.teacher_image)
    ImageView teacherImage;
    @BindView(R.id.teacher_name)
    TextView teacherName;
    @BindView(R.id.teacher_details)
    TextView teacherDetails;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.title)
    TextView title;
    private ArrayList<Fragment> fragmentList;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        com.githang.statusbar.StatusBarCompat.setStatusBarColor(this, Color.parseColor("#F7F8F9"),true);//设置状态栏颜色
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    back.setBackground(ContextCompat.getDrawable(TeacherActivity.this,R.drawable.back_yuanjiao));
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);

                }else if(state == State.COLLAPSED){
                    back.setBackground(ContextCompat.getDrawable(TeacherActivity.this,R.drawable.back_yuanjiao_zd));
                    back.setColorFilter(Color.parseColor("#000000"));
                    title.setVisibility(View.VISIBLE);
                    //折叠状态

                }else {
                    back.setBackground(ContextCompat.getDrawable(TeacherActivity.this,R.drawable.back_yuanjiao));
                    //中间状态
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);
                }
            }
        });
        ViewTreeObserver observer = teacherDetails.getViewTreeObserver(); // textAbstract为TextView控件
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = teacherDetails.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
                Layout l = teacherDetails.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            teacherDetails.setOnClickListener(view -> {
                                new TextPopupwindow().show(TeacherActivity.this, teacherDetails, teacherDetails.getText().toString());
                            });
                        }
                    }
                }
            }
        });
        GlideRound(getIntent().getStringExtra("headimage"),teacherImage);
        teacherName.setText(getIntent().getStringExtra("name"));
        teacherDetails.setText(getIntent().getStringExtra("details"));
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.addTab(mTabLayout.newTab().setText("作品欣赏"));
        mTabLayout.addTab(mTabLayout.newTab().setText("主讲课程"));
        mTabLayout.addTab(mTabLayout.newTab().setText("商品推荐"));
        inits();
    }
    private void inits(){
        if(fragmentList==null)
            fragmentList = new ArrayList<Fragment>();
        Teacher_f1 f1 = new Teacher_f1();
        Bundle bundle1 = new Bundle();
        bundle1.putString("id",getIntent().getStringExtra("id"));
        f1.setArguments(bundle1);
        Teacher_f2 f2 = new Teacher_f2();
        f2.setArguments(bundle1);
        Teacher_f3 f3 = new Teacher_f3();
        f3.setArguments(bundle1);
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"作品欣赏", "主讲课程", "商品推荐"}));
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        http();
    }
    @Override
    public String[] getKey() {
        return new String[]{"id", "name", "headimage", "details"};
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.teacher_layout;
    }


    @OnClick(R.id.back_lin)
    public void onViewClicked() {
        finish();
    }

    private void http(){
        HashMap map=new HashMap<>();
        map.put("id",getIntent().getStringExtra("id"));
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=teacher&op=getMoreInfo", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject object = new JSONObject(text);
                    if (object.getString("code").equals("1")) {
                        JSONObject json_result = object.getJSONObject("result");
                        GlideApp.with(TeacherActivity.this).load(json_result.getString("teacher_background")).centerCrop().into(teacherBr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },map,this);
    }
}
