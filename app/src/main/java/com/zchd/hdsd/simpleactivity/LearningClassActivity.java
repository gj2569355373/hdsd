package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zchd.hdsd.Bin.ClassBin;
import com.zchd.hdsd.Bin.ErJiKeCheng;
import com.zchd.hdsd.Bin.KeCheng;
import com.zchd.hdsd.Bin.ShiZiBin;
import com.zchd.hdsd.Bin.ShiZiBinSerializable;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.fragment.LearningF1;
import com.zchd.hdsd.fragment.LearningF2;
import com.zchd.hdsd.fragment.LearningF3;
import com.zchd.hdsd.view.MainPagerAdapter;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseFragmentActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2017/2/7.
 */
public class LearningClassActivity extends BaseFragmentActivity {
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    List<KeCheng> list_kc = new ArrayList<>();
    List<ShiZiBin> list_sz = new ArrayList<>();
    LearningF3 f3 = null;
    ClassBin classBin = null;
    @BindView(R.id.mainbackdrop)
    ImageView backdrop;
    @BindView(R.id.title)
    TextView title;
    private ArrayList<Fragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        com.githang.statusbar.StatusBarCompat.setStatusBarColor(this, Color.parseColor("#F7F8F9"),true);//设置状态栏颜色
        title.setText("在学班级");
        http();
    }

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.learningclass_layout;
    }

    private void init() {
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.addTab(mTabLayout.newTab().setText("简介"));
        mTabLayout.addTab(mTabLayout.newTab().setText("课程"));
        mTabLayout.addTab(mTabLayout.newTab().setText("师资"));
        Glide.with(LearningClassActivity.this).load(classBin.getThumb()).into(backdrop);
        fragmentList = new ArrayList<Fragment>();
        LearningF1 f1 = new LearningF1();
        Bundle bundle1 = new Bundle();
        bundle1.putString("data", classBin.getDescription());
        f1.setArguments(bundle1);
        LearningF2 f2 = new LearningF2();
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("data", new ErJiKeCheng("", "", list_kc, ""));
        f2.setArguments(bundle2);
        f3 = new LearningF3();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("data", new ShiZiBinSerializable(list_sz));
        f3.setArguments(bundle3);
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"简介", "课程", "师资"}));
        mTabLayout.setupWithViewPager(viewPager);
    }

    private void http() {
        Map map = new HashMap();
        map.put("userId", HdsdApplication.id);
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=class&op=myClassInfo", new TextLinstener(LearningClassActivity.this) {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
//                Toast.makeText(LearningClassActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONObject classjsobject = result.getJSONObject("class");
                        //班级信息
                        classBin = new ClassBin(classjsobject.getString("id"), classjsobject.getString("className"), classjsobject.getString("companyId"), classjsobject.getString("status"), classjsobject.getString("deleted"), classjsobject.getString("thumb"), classjsobject.getString("description"));
                        //课程信息
                        JSONArray courseArray = result.getJSONArray("course");
                        for (int i = 0; i < courseArray.length(); i++) {
                            JSONObject coursejsobject = courseArray.getJSONObject(i);
                            list_kc.add(new KeCheng(coursejsobject.getString("courseName"), coursejsobject.getString("thumb"), coursejsobject.getString("id"), coursejsobject.getString("parentCourseId"), coursejsobject.getJSONArray("hasChildren").length() == 0 ? false : true));
                        }
                        //师资信息
                        JSONArray teacherArray = result.getJSONArray("teacher");
                        for (int i = 0; i < teacherArray.length(); i++) {
                            JSONObject teacherjsobject = teacherArray.getJSONObject(i);
                            list_sz.add(new ShiZiBin(teacherjsobject.getString("avatar"), teacherjsobject.getString("nickname"), teacherjsobject.getString("remark"), teacherjsobject.getString("description")));
                        }
//                        Toast.makeText(LearningClassActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                        init();
                    } else
                        Toast.makeText(LearningClassActivity.this, "获取失败", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(LearningClassActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, map, LearningClassActivity.this, "获取中");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // TODO
            if (f3 != null) {
                if (f3.learningShiziPop != null) {
                    f3.onKeyDown();
                    return true;
                } else
                    return super.onKeyDown(keyCode, event);

            } else
                return super.onKeyDown(keyCode, event);

        }
        return super.onKeyDown(keyCode, event);
    }
}
