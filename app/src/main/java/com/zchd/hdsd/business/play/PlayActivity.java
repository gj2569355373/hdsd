package com.zchd.hdsd.business.play;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;
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
 * Created by apple on 2018/4/19.
 * 免费专区进入
 *
 */

public class PlayActivity extends BaseActivity  {
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
    private VDVideoView mVDVideoView = null;
    long time1;
    long time2;


	@Override
	public String[] getKey() {
		return new String[]{"id","url","title","type","details","time_size","release_time","play_size","teacher_id","teacher_name","teacher_details","teacher_image","background_image"};
	}

    @Override
    protected void setComponent(ActivityComponent activityComponent) {

    }
    @Override
    protected boolean getStyleColor() {
        return false;
    }
    @Override
    protected void setDataBinding(ViewDataBinding dataBinding) {
        title.setText("作品详情");
        if (!getIntent().getStringExtra("teacher_id").equals("")) {
            play_teacher_lin.setVisibility( View.VISIBLE);
            GlideRound(getIntent().getStringExtra("teacher_image"), teacher_image);
            play_teacher_name.setText(getIntent().getStringExtra("teacher_name"));
        }
        play_title.setText(getIntent().getStringExtra("title"));
        play_time_size.setText(getIntent().getStringExtra("release_time")+" | "
                +getIntent().getStringExtra("play_size")+"人观看");
        bf_neirong.setText(getIntent().getStringExtra("details"));
        time1=System.currentTimeMillis();//记录开始时间
        // 从layout里面得到播放器ID
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
                .getParent());

        VDVideoListInfo infoList = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();
        infoList.mInsertADType = VDVideoListInfo.INSERTAD_TYPE_NONE;//无广告类型
        info.mTitle = getIntent().getStringExtra("title");
        info.mPlayUrl =getIntent().getStringExtra("url");
//        if (getIntent().getStringExtra("type").equals("4"))
//            info.mVideoDuration=Long.parseLong(getIntent().getStringExtra("time_size"));
        infoList.addVideoInfo(info);
        // 初始化播放器以及播放列表
        mVDVideoView.open(PlayActivity.this, infoList);
        // 开始播放，直接选择序号即可
        mVDVideoView.play(0);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.free_play_layout;
    }
    @OnClick({R.id.back, R.id.play_teacher_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.play_teacher_lin:
                Intent intent = new Intent(PlayActivity.this, TeacherActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("teacher_id"));
                intent.putExtra("name", getIntent().getStringExtra("teacher_name"));
                intent.putExtra("details", getIntent().getStringExtra("teacher_details"));
                intent.putExtra("headimage", getIntent().getStringExtra("teacher_image"));
                startActivity(intent);
                break;
        }
    }
    private void httppost() {
        Map<String, String>map =new HashMap<String, String>();;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        time2=System.currentTimeMillis();
        httppost();
        mVDVideoView.release(true);//释放资源
        super.onDestroy();

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mVDVideoView.onResume();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mVDVideoView.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mVDVideoView.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
//            background_image.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
        }
    }


}