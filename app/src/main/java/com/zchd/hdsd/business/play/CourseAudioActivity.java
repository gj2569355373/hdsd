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
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import com.zchd.hdsd.view.AudioWife;
import okhttp3.Call;

/**
 * Created by apple on 2018/5/10.
 */

public class CourseAudioActivity  extends BaseActivity {
    @BindView(R.id.audio_viewgroup)
    RelativeLayout audio_viewgroup;
    @BindView(R.id.background_image)
    ImageView background_image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bf_neirong)
    TextView bf_neirong;
    @BindView(R.id.bf_zi)
    TextView bf_zi;
    long time1;
    long time2;
    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @Override
    public String[] getKey() {
        return new String[]{"courseId","vedioId","title","url","text","time_size","type","background_image"};
    }


    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        AudioWife.getInstance().init(this, Uri.parse(getIntent().getStringExtra("url")))
                .useDefaultUi(audio_viewgroup, getLayoutInflater());
        title.setText(getIntent().getStringExtra("title"));
        bf_zi.setText(getIntent().getStringExtra("title"));
        bf_neirong.setText(getIntent().getStringExtra("text"));
        time1=System.currentTimeMillis();//记录开始时间
        GlideApp.with(this).load(getIntent().getStringExtra("background_image"))
                .fitCenter().into(background_image);
        bf_neirong.setText(getIntent().getStringExtra("details"));

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.courseaudio_layout;
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    private void httppost() {
        Map<String, String>map =new HashMap<String, String>();;
        map.put("userId", HdsdApplication.id);
        map.put("token",HdsdApplication.token);
        map.put("courseId", getIntent().getStringExtra("courseId"));
        map.put("vedioId", getIntent().getStringExtra("vedioId"));
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
        if (HdsdApplication.login) {
            time2=System.currentTimeMillis();
            httppost();
        }
        super.onDestroy();
    }
}