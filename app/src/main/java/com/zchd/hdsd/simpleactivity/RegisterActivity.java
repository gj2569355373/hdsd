package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.main.MainActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.hdsd.view.country.CountryModel;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import okhttp3.Call;

/**
 * 注册页面
 * 
 * */
public class RegisterActivity extends BaseActivity implements OnClickListener{
	private EditText regisrer_number,regisrer_yzm,regisrer_password,regisrer_cpm;
	private TextView title,register_yhxy,register,regisrer_hqyzm,rcountry_textview;
	private ImageButton back,register_xy;
	private RelativeLayout rcountry_relatlayout;
	boolean mark_xy=false;
	Handler handler=new Handler();
	String registerType="0";
	SharedPreferences_operate operate;
	CountDownTimer countDownTimer;
	GradientDrawable myGrad;
	CountryModel countryModel;
//	protected VolleyHttp voleyhttp;
	int timer=60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		operate = new SharedPreferences_operate("login", RegisterActivity.this);
		countryModel=new CountryModel("中国大陆","+86");
		findbyid();
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.register_layout;
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

	private void findbyid() {
		// TODO Auto-generated method stub

		rcountry_relatlayout= (RelativeLayout) findViewById(R.id.rcountry_relatlayout);
		rcountry_relatlayout.setOnClickListener(view -> startActivityForResult(
				new Intent(RegisterActivity.this,CountryActivity.class),5));
		rcountry_textview= (TextView) findViewById(R.id.rcountry_textview);
		rcountry_textview.setText(countryModel.countryName+" "+countryModel.countryNumber);
		back=(ImageButton) findViewById(R.id.back);
		back.setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            finish();
        });
		title=(TextView) findViewById(R.id.title);
		title.setText("注册");

		regisrer_number=(EditText) findViewById(R.id.regisrer_number);

		
		regisrer_yzm=(EditText) findViewById(R.id.regisrer_yzm);

		
		regisrer_password=(EditText) findViewById(R.id.regisrer_password);

		
		regisrer_cpm=(EditText) findViewById(R.id.regisrer_cpm);

		
		register_yhxy=(TextView) findViewById(R.id.register_yhxy);

		register_yhxy.setOnClickListener(this);
		
		regisrer_hqyzm=(TextView) findViewById(R.id.regisrer_hqyzm);

		regisrer_hqyzm.setOnClickListener(this);
		
		register=(TextView) findViewById(R.id.register_register);

		register.setOnClickListener(this);
		
		register_xy=(ImageButton) findViewById(R.id.register_xy);
		register_xy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mark_xy)
				{
					register_xy.setBackgroundResource(R.drawable.radio_off);
					mark_xy=false;
				}
				else
				{
					register_xy.setBackgroundResource(R.drawable.radio_on);
					mark_xy=true;
				}
			}
		});
		myGrad = (GradientDrawable)regisrer_hqyzm.getBackground();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.register_register:
			if(!mark_xy)
			{
				Toast.makeText(RegisterActivity.this, "请同意用户协议！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(regisrer_yzm.getText().toString().length()!=4)
			{
				Toast.makeText(RegisterActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(regisrer_password.getText().toString().length()<6)
			{
				Toast.makeText(RegisterActivity.this, "密码长度大于6位！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!regisrer_cpm.getText().toString().equals(regisrer_password.getText().toString()))
			{
				Toast.makeText(RegisterActivity.this, "请保持密码输入一致！", Toast.LENGTH_SHORT).show();
				return;
			}
			Map<String, String>map1=new HashMap<String, String>();
			map1.put("registerType", registerType);
			map1.put("mobile", regisrer_number.getText().toString());
			map1.put("code", regisrer_yzm.getText().toString());
			map1.put("password", regisrer_password.getText().toString());
			map1.put("deviceCode", HdsdApplication.TelephonyMgr);

			icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=users&op=register", new TextLinstener(RegisterActivity.this) {
				
				@Override
				public void onresponse(String text) {
					// TODO Auto-generated method stub
					try {
						final JSONObject object=new JSONObject(text);
						if(object.getString("code").equals("1"))
						{
							HdsdApplication.mobile=regisrer_number.getText().toString();
							HdsdApplication.nickname=regisrer_number.getText().toString();
							Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
							httplogin();
							finish();
						}
						else
							Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//						Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//						Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//						setResult(RESULT_OK);
//						httplogin();
//						finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//				
				}
				
				@Override
				public void onerrorResponse(Call call, Exception e, int id) {
					// TODO Auto-generated method stub
					
				}
			}, map1, RegisterActivity.this, "请稍候");

			
			break;
		case R.id.register_yhxy:
			Intent intents=new Intent(RegisterActivity.this, WebviewActivity.class);
			intents.putExtra("url", User.url+"/useragreement.html");
			intents.putExtra("title", "注册协议");
			startActivity(intents);
			
			break;
		case R.id.regisrer_hqyzm:
//			if(regisrer_number.getText().toString().length()!=11)
//			{
//				Toast.makeText(RegisterActivity.this, "手机号码长度不对！！", Toast.LENGTH_SHORT).show();
//				return;
//			}
			regisrer_hqyzm.setClickable(false);
			countDownTimer=new CountDownTimer(timer * 1000, 1000) {
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

			//放在网络获取验证码成功的回调上处理
			Map<String, String>map=new HashMap<String, String>();
//			if(regisrer_number.getText().toString().length()!=11)
//			{
//				Toast.makeText(RegisterActivity.this, "手机号长度为11位", Toast.LENGTH_SHORT).show();
//				return;
//			}
			
			map.put("mobile", regisrer_number.getText().toString());
			if (!countryModel.countryNumber.equals("+86"))
				map.put("mobileCode", countryModel.countryNumber.trim());

			icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=users&op=getRegisterSMSVerifyCode", new TextLinstener(RegisterActivity.this) {
				
				@Override
				public void onresponse(String text) {
					// TODO Auto-generated method stub
					try {
						final JSONObject object=new JSONObject(text);
						Toast.makeText(RegisterActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
						if(object.getString("code").equals("1"))
						{
							countDownTimer.start();
						}
						else
							regisrer_hqyzm.setClickable(true);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(RegisterActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
						regisrer_hqyzm.setClickable(true);

					}
				}
				
				@Override
				public void onerrorResponse(Call call, Exception e, int id) {
					Toast.makeText(RegisterActivity.this, "网络错误...", Toast.LENGTH_SHORT).show();
				}
			}, map);
			
			


			
			break;
		default:
			break;
		}
	}
	void httplogin(){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", regisrer_number.getText().toString());
		map.put("password", regisrer_password.getText().toString());
		map.put("deviceCode", HdsdApplication.TelephonyMgr);
		icssOkhttp.HttppostString(User.url + "/index.php?mod=site&name=api&do=users&op=login",
				new TextLinstener(RegisterActivity.this) {

					@Override
					public void onresponse(String text) {
						// TODO Auto-generated method stub
						try {
							JSONObject object = new JSONObject(text);
							if (object.getString("code").equals("1")) {
								setlogindata(object.getJSONObject("result"));
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

	void setlogindata(JSONObject object_result) {
		try {
			operate.addint("islogin", 1);
			operate.addString("token", object_result.getString("token"));
			HdsdApplication.login = true;
			HdsdApplication.token = object_result.getString("token");
			operate.addString("password", regisrer_password.getText().toString());
			JSONObject object_user = object_result.getJSONObject("user");
			HdsdApplication.id = object_user.getString("id");
			operate.addString("username", object_user.getString("mobile"));
			operate.addString("pwd", object_user.getString("pwd"));
			//4/15修改屏蔽
			HdsdApplication.getInstance().unReadNotice = object_result.getString("unReadNotice").equals("1");
			if (object_user.getString("userType").equals("4") && !object_user.getString("classId").equals("0"))
				HdsdApplication.getInstance().userType = true;//4/15修改VideoApplication.getInstance().userType=true;
			else
				HdsdApplication.getInstance().userType = false;
			operate.addString("userType", object_user.getString("userType"));
			operate.addString("classId", object_user.getString("classId"));
			//4/15修改屏蔽
			HdsdApplication.mobile = object_user.getString("mobile");
			HdsdApplication.avatar = object_user.getString("avatar");
			HdsdApplication.qqName = object_user.getString("qqName");
			HdsdApplication.weixinName = object_user.getString("weixinName");
			HdsdApplication.weiboName = object_user.getString("weiboName");
			HdsdApplication.nickname = object_user.getString("nickname");
			HdsdApplication.hasUnRead = object_result.getString("hasUnRead");
			//User.myActiveCourseIds = object_result.getString("myActiveCourseIds");
			if (User.list_courseId.size() != 0) {
				User.list_courseId.clear();
			}
			User.list_courseId.addAll(User.setStringList(object_result.getString("myActiveCourseIds")));
			setResult(RESULT_OK);
			setResult(RESULT_OK);
			MainActivity.Refresh = true;
			MainActivity.Refresh_GWC = true;
			MainActivity.Refresh_KC = true;
			finish();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(countDownTimer!=null) {
			countDownTimer.cancel();
			countDownTimer.onFinish();
		}
	}

}
