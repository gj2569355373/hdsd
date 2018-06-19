package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.ResponseBean;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.view.country.CountryModel;
import com.zchd.library.network.linstener.TextLinstener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import okhttp3.Call;

public class WangJiMiMaActivity extends BaseActivity{
	EditText regisrer_number,xinmima,zaicixinmima,regisrer_yzm;
	TextView regisrer_hqyzm,tijiao,rcountry_textview;
	private RelativeLayout rcountry_relatlayout;
	Handler handler=new Handler();
	CountDownTimer mCountDownTimer;
	int timer=60;
	GradientDrawable myGrad;
	CountryModel countryModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		countryModel=new CountryModel("中国大陆","+86");
		findview();
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.wjmm_layout;
	}

	@Override
	protected void onDestroy() {
		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
			mCountDownTimer.onFinish();
		}
		super.onDestroy();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==5&&resultCode==RESULT_OK)
		{
			if (data!=null)
			{
				countryModel.countryNumber=data.getStringExtra("countryNumber").trim();
				countryModel.countryName=data.getStringExtra("countryName").trim();
				rcountry_textview.setText(countryModel.countryName+" "+countryModel.countryNumber);
			}
		}
	}


	private void findview() {
		// TODO Auto-generated method stub
		rcountry_relatlayout= (RelativeLayout) findViewById(R.id.rcountry_relatlayout);
		rcountry_relatlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(WangJiMiMaActivity.this,CountryActivity.class),5);
			}
		});
		rcountry_textview= (TextView) findViewById(R.id.rcountry_textview);
		rcountry_textview.setText(countryModel.countryName+" "+countryModel.countryNumber);
		ImageButton back=(ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView title=(TextView) findViewById(R.id.title);
		title.setText("忘记密码");
		
		regisrer_number=(EditText) findViewById(R.id.regisrer_number);

		xinmima=(EditText) findViewById(R.id.xinmima);

		zaicixinmima=(EditText) findViewById(R.id.zaicixinmima);

		regisrer_yzm=(EditText) findViewById(R.id.regisrer_yzm);

		regisrer_hqyzm=(TextView) findViewById(R.id.regisrer_hqyzm);

		tijiao=(TextView) findViewById(R.id.tijiao);

		regisrer_hqyzm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//放在网络获取验证码成功的回调上处理
				Map<String, String>map=new HashMap<String, String>();
//				if(regisrer_number.getText().length()!=11) {
//					Toast.makeText(WangJiMiMaActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
//					return;
//				}
				regisrer_hqyzm.setClickable(false);
				mCountDownTimer = new CountDownTimer(timer * 1000, 1000) {
					@Override
					public void onFinish() {
						// done
						regisrer_hqyzm.setText("获取验证码");
						regisrer_hqyzm.setClickable(true);
						myGrad.setColor(0xFFEF5829);

						timer = 60;
					}

					@Override
					public void onTick(long arg0) {
						// 每1000毫秒回调的方法
						regisrer_hqyzm.setText("重新发送(" + timer + ")");
						regisrer_hqyzm.setTextColor(Color.WHITE);
						myGrad.setColor(0xFFCACACA);
						regisrer_hqyzm.setClickable(false);
						timer--;
					}
				};
				map.put("mobile", regisrer_number.getText().toString());
				if (!countryModel.countryNumber.equals("+86"))
					map.put("mobileCode", countryModel.countryNumber.trim());
				icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=users&op=getSMSVerifyCode", new TextLinstener(WangJiMiMaActivity.this) {
					
					@Override
					public void onresponse(String text) {
						// TODO Auto-generated method stub
						try {
							final JSONObject object=new JSONObject(text);
							Toast.makeText(WangJiMiMaActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
							if(object.getString("code").equals("1"))
							{
								mCountDownTimer.start();
							}
							else
								regisrer_hqyzm.setClickable(true);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onerrorResponse(Call call, Exception e, int id) {
						Toast.makeText(WangJiMiMaActivity.this, "网络错误...", Toast.LENGTH_SHORT).show();
					}
				}, map,WangJiMiMaActivity.this,"获取中");
			}
		});
		
		tijiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(regisrer_number.getText().toString().equals(""))
				{
					Toast.makeText(WangJiMiMaActivity.this, "手机号不可为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(xinmima.getText().toString().equals(""))
				{
					Toast.makeText(WangJiMiMaActivity.this, "新密码不可为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!xinmima.getText().toString().equals(zaicixinmima.getText().toString()))
				{
					Toast.makeText(WangJiMiMaActivity.this, "两次密码输入需要一致！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(regisrer_yzm.getText().toString().equals(""))
				{
					Toast.makeText(WangJiMiMaActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (xinmima.getText().toString().length() < 6) {
					Toast.makeText(WangJiMiMaActivity.this, "密码应该是6位或6位以上至30位的数字+字符！", Toast.LENGTH_SHORT).show();
					return;
				}
				submit();
			}
		});
		myGrad = (GradientDrawable)regisrer_hqyzm.getBackground();
	}
	
	private void submit() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", regisrer_number.getText().toString());
		map.put("password", xinmima.getText().toString());
		map.put("verifyCode", regisrer_yzm.getText().toString());
		map.put("deviceCode", HdsdApplication.TelephonyMgr);
		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=users&op=forgotPwdStepOne", new TextLinstener(WangJiMiMaActivity.this) {

			@Override
			public void onerrorResponse(Call call, Exception e, int id) {
				Log.i("info", "onError");
			}

			@Override
			public void onresponse(String text) {
				Gson gson = new GsonBuilder().create();
				ResponseBean<?> responseBean = gson.fromJson(text, ResponseBean.class);
				if (responseBean.getCode() == 1) {
					Intent intent = new Intent();
					intent.setClass(WangJiMiMaActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				Toast.makeText(WangJiMiMaActivity.this, responseBean.getMessage(), Toast.LENGTH_SHORT).show();
			}}, map, WangJiMiMaActivity.this, "请稍后...");
	}
}
