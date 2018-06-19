package com.zchd.hdsd.business.course;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.Bin.MallBin;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.ShoppingCartInfo;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.main.MainActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.simpleactivity.LoginActivity;
import com.zchd.hdsd.simpleactivity.OrderConfirmActivity;
import com.zchd.hdsd.simpleactivity.SimpleTestActivity;
import com.zchd.hdsd.view.AppBarStateChangeListener;
import com.zchd.hdsd.view.MainPagerAdapter;
import com.zchd.hdsd.wxapi.WxPayUtil;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zchd.library.widget.IPopupWindow;
import com.zchd.library.widget.PromptPopwindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseFragmentActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/9.
 */
public class CourseActivity extends BaseFragmentActivity {

    @BindView(R.id.mAppBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.course_image)
    ImageView courseImage;
    @BindView(R.id.course_zan)
    ImageView courseZan;
    @BindView(R.id.course_shoucang)
    ImageView courseShoucang;
    @BindView(R.id.course_botton_left)
    TextView courseBottonLeft;
    @BindView(R.id.course_botton_right)
    TextView courseBottonRight;
    @BindView(R.id.course_botom_lin)
    LinearLayout courseBotomLin;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.title)
    TextView title;
    private MallBin mallBin=null;
    private String paly_details="";
    private String  play_time="";
    private String play_url="";
    private String play_id="";
    private String play_name="";
    //判断是否已经点赞
    private boolean isLike = false;
    //判断是否已经收藏
    private boolean isFav = false;
    private ArrayList<Fragment> fragmentList;
    SharedPreferences_operate operate;
    private boolean wxb=false;
    private WxPayUtil wxPayUtil;
    private Handler handler=new Handler();

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        com.githang.statusbar.StatusBarCompat.setStatusBarColor(this, Color.parseColor("#F7F8F9"),true);//设置状态栏颜色
        operate=new SharedPreferences_operate(User.hdsd,this);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    back.setBackground(ContextCompat.getDrawable(CourseActivity.this,R.drawable.back_yuanjiao));
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);

                }else if(state == State.COLLAPSED){
                    back.setBackground(ContextCompat.getDrawable(CourseActivity.this,R.drawable.back_yuanjiao_zd));
                    back.setColorFilter(Color.parseColor("#000000"));
                    title.setVisibility(View.VISIBLE);
                    //折叠状态

                }else {
                    back.setBackground(ContextCompat.getDrawable(CourseActivity.this,R.drawable.back_yuanjiao));
                    //中间状态
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);
                }
            }
        });
        GlideApp.with(this).load(getIntent().getStringExtra("image")).into(courseImage);
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.addTab(mTabLayout.newTab().setText("章节"));
        mTabLayout.addTab(mTabLayout.newTab().setText("简介"));
        mTabLayout.addTab(mTabLayout.newTab().setText("评论"));
        GlideApp.with(CourseActivity.this).load(R.drawable.collect_icon_off).into(courseShoucang);
        GlideApp.with(CourseActivity.this).load(R.drawable.like_icon_off).into(courseZan);
        http(true);
        wxPayUtil = new WxPayUtil();
        wxPayUtil.regToWx();
    }
    private void inits(String play_cs,String details,String courseTitle,String mp4_size_time,String comment_size,String fabulous_size,String teacher_id,String teacher_name,String teacher_details,String teacher_image){
        if(fragmentList==null) {
            fragmentList = new ArrayList<Fragment>();
            Course_F1 f1 = new Course_F1();
            Bundle bundle1 = new Bundle();
            bundle1.putString("id", getIntent().getStringExtra("id"));
            bundle1.putString("isPlay", (courseBottonLeft.getText().toString().equals(getString(R.string.free)) || courseBottonRight.getText().toString()
                    .equals(getString(R.string.add_course_ed)) ? "1" : "0"));
            f1.setArguments(bundle1);
            Course_F2 f2 = new Course_F2();
            Bundle bundle2 = new Bundle();
            bundle2.putString("details", details);
            bundle2.putString("courseTitle", courseTitle);
            bundle2.putString("mp4_size_time", mp4_size_time);
            bundle2.putString("comment_size", comment_size);
            bundle2.putString("fabulous_size", fabulous_size);
            bundle2.putString("teacher_id", teacher_id);
            bundle2.putString("teacher_name", teacher_name);
            bundle2.putString("teacher_image", teacher_image);
            bundle2.putString("teacher_details", teacher_details);
            bundle2.putString("play_cs", play_cs);

            if (mallBin != null)
                bundle2.putSerializable("mall", mallBin);
            f2.setArguments(bundle2);
            Course_F3 f3 = new Course_F3();
            Bundle bundle3 = new Bundle();
            bundle3.putString("id", getIntent().getStringExtra("id"));
            f3.setArguments(bundle3);
            fragmentList.add(f1);
            fragmentList.add(f2);
            fragmentList.add(f3);
            viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragmentList, new String[]{"章节", "简介", "评论"}));
            mTabLayout.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(3);
        }
        else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("id", getIntent().getStringExtra("id"));
            bundle1.putString("isPlay", (courseBottonLeft.getText().toString().equals(getString(R.string.free)) || courseBottonRight.getText().toString()
                    .equals(getString(R.string.add_course_ed)) ? "1" : "0"));
            fragmentList.get(0).setArguments(bundle1);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.course_layout_a;
    }

    @Override
    public String[] getKey() {
        return new String[]{"id","title","image"};
    }

    @OnClick({R.id.course_play, R.id.course_zan, R.id.course_shoucang, R.id.back_lin,R.id.course_botton_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.course_play://播放
                if (!play_url.equals("")) {
                    Intent intent = new Intent(CourseActivity.this, SimpleTestActivity.class);
                    intent.putExtra("url", play_url);
                    intent.putExtra("title", play_name);
                    intent.putExtra("text", paly_details);
                    intent.putExtra("courseId", getIntent().getStringExtra("id"));
                    intent.putExtra("vedioId", play_id);
                    intent.putExtra("time_size", play_time);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                    updataPlaySize();
                }

                break;
            case R.id.course_zan://赞
                if (!HdsdApplication.login)
                    showLogin();
                else
                    http_zan();
                break;
            case R.id.course_shoucang://收藏
                if (!HdsdApplication.login)
                    showLogin();
                else
                    http_sc();
                break;
            case R.id.back_lin:
                finish();
                break;
            case R.id.course_botton_right:
                if (!HdsdApplication.login)
                    showLogin();
                else {
                    if (courseBottonRight.getText().toString().equals(getString(R.string.buy_gl_mall))) {
                        //购买关联商品
                        Intent intent3 = new Intent(CourseActivity.this, OrderConfirmActivity.class);
                        List<ShoppingCartInfo> lists = new ArrayList<ShoppingCartInfo>();
                        lists.add(new ShoppingCartInfo(null, "1", mallBin.getId(), mallBin.getName(), mallBin.getPrice(), mallBin.getHeadimage(), mallBin.getDetails(), 10000));
                        intent3.putExtra("goodsInfo", (Serializable) lists);
                        startActivity(intent3);
                    } else if (courseBottonRight.getText().toString().equals(getString(R.string.add_course))) {
                        httpAddCourse();
                    }
                }
                break;
        }
    }
    public void updataPlaySize(){
        if (fragmentList.get(1)!=null)
            ((Course_F2)fragmentList.get(1)).updataplaysize();
    }
    private void httpAddCourse(){
        Map<String, String> map=new HashMap<String, String>();
        map.put("course_id",getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=payMyCourse", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject=new JSONObject(text);
                    if (jsonObject.getString("code").equals("1")) {
                        //添加成功
                        MainActivity.Refresh_KC=true;
                        showShortToast(getString(R.string.add_course_toast));
                        http(true);
                    }
                    else if(jsonObject.getString("code").equals("2")){
                        //支付
                        wxb=true;
                        wxPayUtil.pay(jsonObject.getJSONObject("result"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
                }
            }
        },map,this,"添加中");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wxb)
        {//处理微信回调

            if (operate.getString("wxpaycode").equals("1")){
                showProgressDialog("获取支付结果");
                handler.postDelayed(() -> {
                    dimssProgressDialog();
                    http(true);
                }, 2000);
            }
            else if (operate.getString("wxpaycode").equals("-1"))
            {
                showToast("支付失败");
            }
            operate.addString("wxpaycode","0");
            wxb=false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {//个人信息更新
            http(true);
        }
    }
    public void showLogin() {
        new PromptPopwindow().showAlertDialog(CourseActivity.this, courseBotomLin, "是否开始登录？", "是", "否", new IPopupWindow() {
            @Override
            public void cancel() {
            }

            @Override
            public void Oks() {
                startActivityForResult(new Intent(CourseActivity.this, LoginActivity.class),7);
            }
        });
    }

    private void http(boolean showProgress){
        Map<String, String> map=new HashMap<String, String>();
        map.put("course_id",getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getCourseDetail", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject=new JSONObject(text);
                    if (jsonObject.getString("code").equals("1")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        paly_details=result.getString("play_details");
                        play_time=result.getString("play_time");
                        play_url=result.getString("play_url");
                        play_name=result.getString("play_name");
                        play_id=result.getString("play_id");
                        isFav=result.getString("is_collect").equals("1");
                        isLike=result.getString("is_fabulous").equals("1");
                        GlideApp.with(CourseActivity.this).load(isFav?R.drawable.collect_icon_on:R.drawable.collect_icon_off).into(courseShoucang);
                        GlideApp.with(CourseActivity.this).load(isLike?R.drawable.like_icon_on:R.drawable.like_icon_off).into(courseZan);
                        courseBotomLin.setVisibility(View.VISIBLE);
                        if (result.getString("is_mall").equals("1"))
                        {
                            mallBin=new MallBin(result.getString("mall_id"),result.getString("mall_name"),result.getString("mall_details"),result.getString("mall_image"),result.getString("mall_price"),"");
                            mallBin.setMall_assistant_title(result.getString("mall_assistant_title"));
                            courseBottonRight.setText(getString(R.string.buy_gl_mall));
                            courseBottonLeft.setVisibility(View.GONE);
                        }
                        else{
                            courseBottonLeft.setText(result.getString("price").equals("0.00")?getString(R.string.free):"￥"+result.getString("price"));
                            courseBottonRight.setText(result.getString("is_have").equals("0")?getString(R.string.add_course):getString(R.string.add_course_ed));
                        }
                        inits(result.getString("course_play_size"),result.getString("details"),result.getString("title"),"共"+result.getString("mp4_size")+"个视频,共"+result.getString("time_size")
                                ,result.getString("comment_size"),result.getString("fabulous_size"),result.getString("teacher_id"),result.getString("teacher_name")
                                ,result.getString("teacher_details") ,result.getString("teacher_image"));
                    }
                    else
                        showShortToast(jsonObject.getString("message"));
                } catch (JSONException e) {
                    showShortToast(getString(R.string.json_error));
                    e.printStackTrace();
                }
            }
        },map,this,showProgress?"加载中":null);
    }

    public void http_sc() {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constants.USER_ID, HdsdApplication.id);
        param.put("token", HdsdApplication.token);
        param.put("courseId", getIntent().getStringExtra("id"));
        param.put("favorStatus", isFav?"0":"1");//收藏
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=changeCourseFavorStatus", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject=new JSONObject(text);
                    showShortToast(jsonObject.getString("message"));
                    if (jsonObject.getString("code").equals("1")){
                        isFav=!isFav;
                        GlideApp.with(CourseActivity.this).load(isFav?R.drawable.collect_icon_on:R.drawable.collect_icon_off).into(courseShoucang);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },param,this,"处理中");
    }

    public void http_zan() {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constants.USER_ID, HdsdApplication.id);
        param.put("token", HdsdApplication.token);
        param.put("courseId", getIntent().getStringExtra("id"));
        param.put("likeStatus", isLike?"0":"1");//赞
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=changeCourseLikeStatus", new TextLinstener() {
            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                showShortToast(getString(R.string.http_error));
            }

            @Override
            public void onresponse(String text) {
                try {
                    JSONObject jsonObject=new JSONObject(text);
                    showShortToast(jsonObject.getString("message"));
                    if (jsonObject.getString("code").equals("1")){
                        isLike=!isLike;
                        GlideApp.with(CourseActivity.this).load(isLike?R.drawable.like_icon_on:R.drawable.like_icon_off).into(courseZan);
                        if (fragmentList.get(1)!=null)
                            ((Course_F2)fragmentList.get(1)).updataFabulousSize(isLike);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },param,this,"处理中");
    }
}
