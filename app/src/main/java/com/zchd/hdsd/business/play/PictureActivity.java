package com.zchd.hdsd.business.play;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.teacher.TeacherActivity;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by apple on 2018/4/20.
 */

public class PictureActivity extends BaseActivity {
    @BindView(R.id.picture_image)
    ImageView picture_image;
    @BindView(R.id.teacher_image)
    ImageView teacher_image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.play_title)
    TextView play_title;
    @BindView(R.id.play_time_size)
    TextView play_time_size;
    @BindView(R.id.bf_neirong)
    TextView bf_neirong;
    @BindView(R.id.play_teacher_name)
    TextView play_teacher_name;
    @BindView(R.id.play_teacher_lin)
    LinearLayout play_teacher_lin;

    @Override
    public String[] getKey() {
        return new String[]{"id","url","title","details","time_size","release_time","play_size","teacher_id","teacher_name","teacher_details","teacher_image"};
    }

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText("作品详情");
        play_teacher_lin.setVisibility(getIntent().getStringExtra("teacher_id").equals("")? View.GONE:View.VISIBLE);
        GlideRound(getIntent().getStringExtra("teacher_image"),teacher_image);
        play_title.setText(getIntent().getStringExtra("title"));
        play_time_size.setText(getIntent().getStringExtra("release_time")+" | "
                +getIntent().getStringExtra("play_size")+"浏览");
        bf_neirong.setText(getIntent().getStringExtra("details"));
        play_teacher_name.setText(getIntent().getStringExtra("teacher_name"));
        GlideApp.with(PictureActivity.this).load(getIntent().getStringExtra("url")).fitCenter().into(picture_image);
    }

    @OnClick({R.id.back, R.id.play_teacher_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.play_teacher_lin:
                Intent intent = new Intent(PictureActivity.this, TeacherActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("teacher_id"));
                intent.putExtra("name", getIntent().getStringExtra("teacher_name"));
                intent.putExtra("details", getIntent().getStringExtra("teacher_details"));
                intent.putExtra("headimage", getIntent().getStringExtra("teacher_image"));
                startActivity(intent);
                break;

        }
    }
    private void httppost() {
        Map<String, String> map =new HashMap<String, String>();
        map.put("userId", HdsdApplication.id);
        map.put("token",HdsdApplication.token);
        map.put("courseId", getIntent().getStringExtra("courseId"));
        map.put("vedioId", getIntent().getStringExtra("id"));
        map.put("type", getIntent().getStringExtra("type"));
        map.put("accessTime","2200");
        map.put("deviceCode", HdsdApplication.TelephonyMgr);

        icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=vedio&op=addVedioLog", new TextLinstener(this) {

            @Override
            public void onresponse(String text) {
                // TODO Auto-generated method stub
                try {
                    JSONObject object=new JSONObject(text);
                    if(object.getString("code").equals("1"))
                    {
                        //	Toast.makeText(SimpleTestActivity.this, (time2-time1)/60+"分", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void onerrorResponse(Call call, Exception e, int id) {
                // TODO Auto-generated method stub

            }
        }, map);
    }
    @Override
    protected void onDestroy() {
        httppost();
        super.onDestroy();
    }
    @Override
    protected int getLayoutResID() {
        return R.layout.picture_layout;
    }
}
