package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class YiJianFanKuiActivity extends BaseActivity {
	@BindView(R.id.title)
	TextView title;
	@BindView(R.id.back)
	ImageButton back;
	@BindView(R.id.pinlun_EditText)
	EditText pinlun_EditText;
	@BindView(R.id.fabiaopinglun)
	TextView fabiaopinglun;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		fabiaopinglun.setText("提交意见");
		title.setText("意见反馈");
		pinlun_EditText.setHint("请输入您的建议 , 我们会做得更好 。感谢支持!");
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		// TODO Auto-generated method stub
		return R.layout.pinlun_layout;
	}
	@OnClick(R.id.fabiaopinglun)
	public void onBackClick() {
		if (pinlun_EditText.getText().toString().trim().length()<5)
		{
			Toast.makeText(YiJianFanKuiActivity.this,"请输入不少于5个字的意见！",Toast.LENGTH_SHORT).show();
			return;
		}

		Map<String, String>map=new HashMap<String, String>();
		map.put("userId", HdsdApplication.id);
//		map.put("token",User.token);
		map.put("advice",pinlun_EditText.getText().toString());
		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=advice&op=addAdvice", new TextLinstener(YiJianFanKuiActivity.this) {
			
			@Override
			public void onresponse(String text) {
				// TODO Auto-generated method stub
				try {
					JSONObject object=new JSONObject(text);
					if(object.getString("code").equals("1"))
					{
						Toast.makeText(YiJianFanKuiActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
						finish();
					}
					else
						Toast.makeText(YiJianFanKuiActivity.this,object.getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onerrorResponse(Call call, Exception e, int id) {
				// TODO Auto-generated method stub
				
			}
		}, map, YiJianFanKuiActivity.this, "反馈中...");
	}
}
