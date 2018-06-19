package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zchd.hdsd.Bin.Constants;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.bean.ResponseBean;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.network.linstener.TextLinstener;
import com.zchd.library.sqllites.SharedPreferences_operate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class PasswordResetActivity extends BaseActivity {
	
//	@Bind(R.id.reset_pwd_phone)
//	TextView mTextViewPhone;
//	@Bind(R.id.reset_pwd_old)
//	TextView mTextViewOld;
	@BindView(R.id.reset_pwd_new)
	TextView mTextViewNew;
	@BindView(R.id.reset_pwd_confirm)
	TextView mTextViewConfirm;
//	@Bind(R.id.reset_pwd_vericode)
//	TextView mTextViewVericode;

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_reset_pwd;
	}
	/**
	 * 提交
	 */
	@OnClick(R.id.reset_pwd_submit)
	public void onClickSubmit() {
		String newPwd = mTextViewNew.getText().toString();
		String confirmPws = mTextViewConfirm.getText().toString();
		if (newPwd.length()<6) {
			Toast.makeText(PasswordResetActivity.this, "密码长度不得小于6位！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!newPwd.equals(confirmPws)) {
			Toast.makeText(PasswordResetActivity.this, "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
	
		Map<String, String> param = new HashMap<String, String>();
		param.put(Constants.USER_ID, HdsdApplication.id);
		param.put(Constants.TOKEN, HdsdApplication.token);
		param.put("password", newPwd);
		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=users&op=modifyPassword", new TextLinstener(PasswordResetActivity.this) {

			@Override
			public void onerrorResponse(Call call, Exception e, int id) {
				Toast.makeText(PasswordResetActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onresponse(String text) {
				try {
					JSONObject jsonObject=new JSONObject(text);
					showToast(jsonObject.getString("message"));
					if (jsonObject.getString("code").equals("1"))
					{
						SharedPreferences_operate operate=new SharedPreferences_operate("login",PasswordResetActivity.this);
						operate.addString("password", newPwd);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
//
			}}, param,this,"修改中");
	}
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			ImageButton back=(ImageButton) findViewById(R.id.back);
			back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			TextView title=(TextView) findViewById(R.id.title);
			title.setText("修改密码");
		
		//	title.setTypeface(Uitl.tf);
		}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

}
