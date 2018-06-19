package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.main.MainActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.tool.FileUtil;
import com.zchd.hdsd.tool.UpdateVersionTool;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;
import com.zhy.http.okhttp.callback.BitmapCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;

import base.GlideApp;
import okhttp3.Call;

public class SplashActivity extends BaseActivity{
	private LinearLayout L1;
	private SharedPreferences_operate operate;
	ImageView welcome;
	Handler handler=new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		operate=new SharedPreferences_operate("login", getApplicationContext());
		if(operate.getint("start")!=1) {
			Intent intent = new Intent(SplashActivity.this, StartActivity.class);//进入引导页
			startActivity(intent);
			finish();
		}
		else {
			L1 = (LinearLayout) findViewById(R.id.splash_layout_L1);
			welcome = (ImageView) findViewById(R.id.welcome);
			try {
				if (operate.getint("appcode")!= UpdateVersionTool.getVersionCode()) {
				}
				else
				{
					UpDate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (FileUtil.isFileExit(FileUtil.getpath("startthumb"))) {
				Log.e("tag", "starturl-----" + FileUtil.getpath("startthumb"));
				Glide.with(SplashActivity.this).load(FileUtil.getpath("startthumb")).into(welcome);
			} else
				GlideApp.with(SplashActivity.this).load(R.drawable.welcome_start).into(welcome);
			starthttp();
		/*开启动画效果*/
			AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
			animation.setDuration(1500);
			L1.startAnimation(animation);
		}
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.splash_layout;
	}


	private void starthttp() {

		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=sys&op=getGuidePic", new TextLinstener(this) {
			@Override
			public void onerrorResponse(Call call, Exception e, int id) {

			}

			@Override
			public void onresponse(String text) {
				try {
					JSONObject json=new JSONObject(text);
					if (json.getString("code").equals("1"))
					{
						JSONObject result=json.getJSONObject("result");
						if (result.getString("thumb").equals(""))
							return;
						if (operate.getString("starturl").equals(result.getString("thumb")))
						{
							if (!FileUtil.isFileExit(FileUtil.getpath("startthumb")))//文件不存在
							{
								icssOkhttp.okhttpImage(result.getString("thumb"), new BitmapCallback() {
									@Override
									public void onError(Call call, Exception e, int id) {

									}
									@Override
									public void onResponse(Bitmap response, int id) {
										FileUtil.saveBitmaps(response,"startthumb");
									}
								});
							}
						}
						else {
							String url=result.getString("thumb");
							icssOkhttp.okhttpImage(result.getString("thumb"), new BitmapCallback() {
								@Override
								public void onError(Call call, Exception e, int id) {

								}
								@Override
								public void onResponse(Bitmap response, int id) {
									FileUtil.saveBitmaps(response,"startthumb");
									operate.addString("starturl",url);
								}
							});
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		},new HashMap<String, String>());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setnew();//开启自动登录
	}
	void setnew(){
		new Thread(() -> {
            if (operate.getint("islogin")!=0) {
                http();
                long start = System.currentTimeMillis();
                long costTime = System.currentTimeMillis() - start;

                //等待sleeptime时长
                if (1500 - costTime > 0) {
                    try {
                        Thread.sleep(10000 - costTime);
                        int[] locations = new  int[2] ;//解决popupwindow显示问题
                        L1.getLocationOnScreen(locations);
                        android.util.Log.e("tag",locations.toString());
                        HdsdApplication.setAvailableHight(locations[1]+L1.getHeight());//获取不含底部操作栏的坐标值
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                //进入主页面
                //后台登录
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }).start();
	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode==1&&resultCode==200) {
//		//	finish();
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//			android.os.Process.killProcess(android.os.Process.myPid());
//		}
//	}
	void http(){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", operate.getString("username"));
		map.put("password", operate.getString("password"));
		map.put("deviceCode", HdsdApplication.TelephonyMgr);
		icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=users&op=login",
				new TextLinstener(SplashActivity.this) {

					@Override
					public void onresponse(String text) {
						// TODO Auto-generated method stub
						try {
							JSONObject object = new JSONObject(text);
							if (object.getString("code").equals("1")) {
								HdsdApplication.login = true;
								operate.addint("islogin", 1);
//								operate.addString("username", username.getText().toString());
//								operate.addString("password", password.getText().toString());
								JSONObject object_result = object.getJSONObject("result");
								operate.addString("token", object_result.getString("token"));
								HdsdApplication.token = object_result.getString("token");
								HdsdApplication.mobile = map.get("mobile");
								JSONObject object_user = object_result.getJSONObject("user");
								//4/15修改
								HdsdApplication.getInstance().unReadNotice=object_result.getString("unReadNotice").equals("1");
								if (object_user.getString("userType").equals("4")&&!object_user.getString("classId").equals("0"))
									HdsdApplication.getInstance().userType=true;
								else
									HdsdApplication.getInstance().userType=false;
								//4/15修改
								HdsdApplication.id = object_user.getString("id");
								HdsdApplication.avatar = object_user.getString("avatar");
								HdsdApplication.qqName = object_user.getString("qqName");
								HdsdApplication.weixinName = object_user.getString("weixinName");
								HdsdApplication.weiboName = object_user.getString("weiboName");
								HdsdApplication.nickname = object_user.getString("nickname");
								HdsdApplication.hasUnRead = object_result.getString("hasUnRead");
//								if (HdsdApplication.hasUnRead.equals("1"))
//								{	if (HdsdApplication.hasUnRead.equals("1")) {
//										MainActivity.mImageViewNewsIcon.setImageResource(R.drawable.news_on_icon);
//									} else {
//										MainActivity.mImageViewNewsIcon.setImageResource(R.drawable.news_off_icon);
//									}
//								}
								User.list_courseId.addAll(User.setStringList(object_result.getString("myActiveCourseIds")))	;

							}
							startActivity(new Intent(SplashActivity.this, MainActivity.class));
							finish();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							startActivity(new Intent(SplashActivity.this, MainActivity.class));
							finish();
						}

					}

					@Override
					public void onerrorResponse(Call call, Exception e, int id) {
						// TODO Auto-generated method stub
						handler.postDelayed(() -> {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        },1500);
					}
				}, map);
	}
	/*
	* 检查版本更新
	* **/
	void UpDate(){
		Map<String,String>map =new HashMap<String, String>();
		icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=sys&op=checkAppVersion", new TextLinstener(this) {
			@Override
			public void onerrorResponse(Call call, Exception e, int id) {

			}

			@Override
			public void onresponse(String text) {
				try {
					JSONObject object = new JSONObject(text);
					if (object.getString("code").equals("1")){
						JSONObject result = object.getJSONObject("result");
						SharedPreferences_operate operate=new SharedPreferences_operate("login",HdsdApplication.getInstance());
						Log.e("tag","versionCode"+operate.getint("version"));
						operate.addint("version",result.getInt("versionCode"));
						operate.addString("versiondate",result.getString("updateItems"));//版本内容updateItems
						operate.addString("url",result.getString("url"));
						operate.addString("revision",result.getString("version"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},map);
	}

	@Override
	protected void onDestroy() {
		Log.e("tag","SplashActivity-onDestroy");
		super.onDestroy();
	}

}
