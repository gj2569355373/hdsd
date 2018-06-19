package com.zchd.hdsd.business.play;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.business.teacher.TeacherActivity;
import com.zchd.hdsd.view.AudioWife;
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
 * Created by apple on 2018/5/10.
 */

public class AudioActivity extends BaseActivity {
    @BindView(R.id.audio_viewgroup)
    RelativeLayout audio_viewgroup;
    @BindView(R.id.teacher_image)
    ImageView teacher_image;
    @BindView(R.id.background_image)
    ImageView background_image;
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
    long time1;
    long time2;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @Override
    public String[] getKey() {
        return new String[]{"id","url","title","type","details","time_size","release_time","play_size","teacher_id","teacher_name","teacher_details","teacher_image","background_image"};
    }

    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        AudioWife.getInstance().init(this, Uri.parse(getIntent().getStringExtra("url")))
                .useDefaultUi(audio_viewgroup, getLayoutInflater());

        title.setText("作品详情");
        if (!getIntent().getStringExtra("teacher_id").equals("")) {
            play_teacher_lin.setVisibility( View.VISIBLE);
            GlideRound(getIntent().getStringExtra("teacher_image"), teacher_image);
            play_teacher_name.setText(getIntent().getStringExtra("teacher_name"));
        }
        play_title.setText(getIntent().getStringExtra("title"));
        play_time_size.setText(getIntent().getStringExtra("release_time")+" | "
                +getIntent().getStringExtra("play_size")+"人收听");
        GlideApp.with(this).load(getIntent().getStringExtra("background_image"))
                .centerCrop().into(background_image);
        bf_neirong.setText(getIntent().getStringExtra("details"));
        time1=System.currentTimeMillis();//记录开始时间
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.audio_layout;
    }

    @OnClick({R.id.back, R.id.play_teacher_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.play_teacher_lin:
                Intent intent = new Intent(AudioActivity.this, TeacherActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("teacher_id"));
                intent.putExtra("name", getIntent().getStringExtra("teacher_name"));
                intent.putExtra("details", getIntent().getStringExtra("teacher_details"));
                intent.putExtra("headimage", getIntent().getStringExtra("teacher_image"));
                startActivity(intent);
                break;

        }
    }
    private void httppost() {
        Map<String, String> map =new HashMap<String, String>();;
        map.put("userId", HdsdApplication.id);
        map.put("token",HdsdApplication.token);
        map.put("courseId", getIntent().getStringExtra("courseId"));
        map.put("vedioId", getIntent().getStringExtra("id"));
        map.put("type", getIntent().getStringExtra("type"));
        map.put("accessTime", (time2-time1)+"");
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
    protected void onPause() {
        AudioWife.getInstance().pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AudioWife.getInstance().release();
        AudioWife audioWife = AudioWife.getInstance();
        audioWife = null;
        time2=System.currentTimeMillis();
        httppost();
        super.onDestroy();
    }
}
