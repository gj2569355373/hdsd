package com.zchd.hdsd.simpleactivity;

import android.content.res.Configuration;
import android.databinding.ViewDataBinding;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.network.linstener.TextLinstener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import base.GlideApp;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
 * 视频播放页面
 * 改掉播放错误  play_error_tip
 *
 * 使用Intent需要传的参数
 * url   播放全路径
 * zi 	   字的名字
 * text  书写要点内容
 * courseId      对应课程的id
 * vedioId		    对应视频的id
 * **/
public class SimpleTestActivity extends BaseActivity  {
	@BindView(R.id.title)
	TextView title;
	@BindView(R.id.bf_neirong)
	TextView bf_neirong;
	@BindView(R.id.bf_zi)
	TextView bf_zi;
	private VDVideoView mVDVideoView = null;
	private final static String TAG = "Test1Activity";
	long time1;
	long time2;

	@OnClick(R.id.back)
	public void onViewClicked() {
		finish();
	}
	@Override
	protected boolean getStyleColor() {
		return false;
	}

	@Override
	public String[] getKey() {
		return new String[]{"courseId","vedioId","title","url","text","time_size","type","background_image"};
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {
		title.setText(getIntent().getStringExtra("title"));
		bf_zi.setText(getIntent().getStringExtra("title"));
		bf_neirong.setText(getIntent().getStringExtra("text"));
		time1=System.currentTimeMillis();//记录开始时间
		// 从layout里面得到播放器ID
		mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
		// 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
				.getParent());
		VDVideoListInfo infoList = new VDVideoListInfo();
		VDVideoInfo info = new VDVideoInfo();
		infoList.mInsertADType = VDVideoListInfo.INSERTAD_TYPE_NONE;//无广告
		info.mTitle = getIntent().getStringExtra("title");
		info.mPlayUrl =getIntent().getStringExtra("url");//="http://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3"
//		if (getIntent().getStringExtra("type").equals("2")||getIntent().getStringExtra("type").equals("4")) {
//			info.mVideoDuration = Long.parseLong(getIntent().getStringExtra("time_size"));
//			GlideApp.with(this).load(getIntent().getStringExtra("background_image"))
//					.centerCrop().into(background_image);
//		}
		infoList.addVideoInfo(info);
		// 初始化播放器以及播放列表
		mVDVideoView.open(SimpleTestActivity.this, infoList);
		// 开始播放，直接选择序号即可
		mVDVideoView.play(0);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.simple_test;
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
		if (HdsdApplication.login) {
			time2=System.currentTimeMillis();
			httppost();
		}
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
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mVDVideoView.setIsFullScreen(false);
			LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
		}

	}

}
