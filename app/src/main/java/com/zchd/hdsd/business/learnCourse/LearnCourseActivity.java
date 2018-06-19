package com.zchd.hdsd.business.learnCourse;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.camera.CameraActivity;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.course.CourseActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.teacher.TeacherActivity;
import com.zchd.hdsd.simpleactivity.SimpleTestActivity;
import com.zchd.hdsd.view.AppBarStateChangeListener;
import com.zchd.library.network.linstener.TextLinstener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseFragmentActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by GJ on 2018/4/6.
 * 我的学习课程
 * id
 * title
 * headimage
 */
public class LearnCourseActivity extends BaseFragmentActivity {
    @BindView(R.id.mAppBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.course_image)
    ImageView courseImage;
    @BindView(R.id.course_saoma)
    ImageView courseSaoma;
    @BindView(R.id.course_zan)
    ImageView courseZan;
    @BindView(R.id.course_shoucang)
    ImageView courseShoucang;
    @BindView(R.id.course_title)
    TextView courseTitle;
    @BindView(R.id.course_size_time)
    TextView courseSizeTime;
    @BindView(R.id.course_learn_time)
    TextView courseLearnTime;
    @BindView(R.id.course_teacher_image)
    ImageView courseTeacherImage;
    @BindView(R.id.course_teacher_namee)
    TextView courseTeacherNamee;
    @BindView(R.id.title)
    TextView title;
    private FragmentManager mFragmentManager = null;
    private FragmentTransaction mFragmentTransaction = null;
    public String fragment1Tag = "fragment1", fragment2Tag = "fragment2";
    public String current = "";
    public String paly_details="";
    //判断是否已经点赞
    private boolean isLike = false;
    //判断是否已经收藏
    private boolean isFav = false;
    private String  play_time="";
    private String teacher_id="",teacher_name="",teacher_details="",teacher_image="";
    private String play_url="";
    private String play_id="";
    private String play_name="";
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
                    back.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao));
                    courseSaoma.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao));
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    courseSaoma.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);
                    //展开状态

                }else if(state == State.COLLAPSED){
                    back.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao_zd));
                    courseSaoma.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao_zd));
                    //折叠状态
                    back.setColorFilter(Color.parseColor("#000000"));
                    courseSaoma.setColorFilter(Color.parseColor("#000000"));
                    title.setVisibility(View.VISIBLE);
                }else {
                    back.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao));
                    courseSaoma.setBackground(ContextCompat.getDrawable(LearnCourseActivity.this,R.drawable.back_yuanjiao));
                    //中间状态
                    back.setColorFilter(Color.parseColor("#ffffff"));
                    courseSaoma.setColorFilter(Color.parseColor("#ffffff"));
                    title.setVisibility(View.GONE);
                }
            }
        });

        courseTitle.setText(getIntent().getStringExtra("title"));
        GlideApp.with(this).load(getIntent().getStringExtra("headimage")).into(courseImage);
        http();
        setFragment1();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.learn_course_layout;
    }

    @Override
    public String[] getKey() {
        return new String[]{"id,title,headimage"};
    }


    @OnClick({R.id.course_play, R.id.back_lin, R.id.course_saoma_lin, R.id.course_teacher_image, R.id.course_zan, R.id.course_shoucang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.course_play:
                //视频播放
                Intent intent = new Intent(LearnCourseActivity.this, SimpleTestActivity.class);
                intent.putExtra("url", play_url);
                intent.putExtra("title", play_name);
                intent.putExtra("text", paly_details);
                intent.putExtra("courseId", getIntent().getStringExtra("id"));
                intent.putExtra("vedioId", play_id);
                intent.putExtra("time_size",play_time);
                intent.putExtra("type", "1");
                startActivity(intent);

                break;
            case R.id.back_lin:
                finish();
                break;
            case R.id.course_saoma_lin:
                Intent intentr=new Intent(LearnCourseActivity.this, CameraActivity.class);
                intentr.putExtra("type","1");
                startActivity(intentr);
                //扫码
                break;
            case R.id.course_teacher_image:
                //进入教师详情
                if (!teacher_id.equals("")&&!teacher_id.equals("null")) {
                    Intent intenti = new Intent(LearnCourseActivity.this, TeacherActivity.class);
                    intenti.putExtra("id", teacher_id);
                    intenti.putExtra("name", teacher_name);
                    intenti.putExtra("details", teacher_details);
                    intenti.putExtra("headimage", teacher_image);
                    startActivity(intenti);
                }
                break;
            case R.id.course_zan://赞
                    http_zan();
                break;
            case R.id.course_shoucang://收藏
                    http_sc();
                break;
        }
    }
    public void setFragment1() {
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
            fragment = new Fragment_one();
            Bundle bundle=new Bundle();
            bundle.putString("id",getIntent().getStringExtra("id"));
            fragment.setArguments(bundle);
            mFragmentTransaction.add(R.id.learn_frame, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
            mFragmentTransaction.show(fragment);
        mFragmentTransaction.commit();
    }
    public void setFragment2(String id,String title) {//章节id
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
            fragment = new Fragment_two();
            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            bundle.putString("title",title);
            bundle.putString("courseId",getIntent().getStringExtra("id"));
            fragment.setArguments(bundle);
            mFragmentTransaction.add(R.id.learn_frame, fragment, current);
        } else // 如果Fragment不为空，则直接将它显示出来
        {
            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            bundle.putString("title",title);
            bundle.putString("courseId",getIntent().getStringExtra("id"));
            fragment.setArguments(bundle);
            mFragmentTransaction.show(fragment);
        }
        mFragmentTransaction.setCustomAnimations(R.anim.push_right_in,R.anim.push_right_out);
        mFragmentTransaction.commit();
    }

    private void http(){
        Map<String, String> map=new HashMap<String, String>();
        map.put("course_id",getIntent().getStringExtra("id"));
        map.put("token", HdsdApplication.token);
        icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=course&op=getMyCourseDetail", new TextLinstener() {
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
                        courseSizeTime.setText("共"+result.getString("mp4_size")+"个视频,共"+result.getString("time_size"));
                        courseLearnTime.setText("已学习"+result.getString("progress")+"%,累计"+result.getString("learn_time"));
                        teacher_id=result.getString("teacher_id");
                        if (!teacher_id.equals("")&&!teacher_id.equals("null")) {
                            courseTeacherNamee.setText(result.getString("teacher_name"));
                            GlideRound(result.getString("teacher_image"), courseTeacherImage);
                        }
                        isFav=result.getString("is_collect").equals("1");
                        isLike=result.getString("is_fabulous").equals("1");
                        teacher_name=result.getString("teacher_name");
                        teacher_image=result.getString("teacher_image");
                        teacher_details=result.getString("teacher_details");
                        paly_details=result.getString("play_details");
                        play_time=result.getString("play_time");
                        play_url=result.getString("play_url");
                        play_name=result.getString("play_name");
                        play_id=result.getString("play_id");
                        GlideApp.with(LearnCourseActivity.this).load(result.getString("is_collect").equals("1")?R.drawable.collect_icon_on:R.drawable.collect_icon_off).into(courseShoucang);
                        GlideApp.with(LearnCourseActivity.this).load(result.getString("is_fabulous").equals("1")?R.drawable.like_icon_on:R.drawable.like_icon_off).into(courseZan);
                    }
                    else
                        showShortToast(jsonObject.getString("message"));
                } catch (JSONException e) {
                    showShortToast(getString(R.string.json_error));
                    e.printStackTrace();
                }
            }
        },map,this,"加载中");
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
                        GlideApp.with(LearnCourseActivity.this).load(isFav?R.drawable.collect_icon_on:R.drawable.collect_icon_off).into(courseShoucang);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    showShortToast(getString(R.string.json_error));
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
                        GlideApp.with(LearnCourseActivity.this).load(isLike?R.drawable.like_icon_on:R.drawable.like_icon_off).into(courseZan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },param,this,"处理中");
    }

}
