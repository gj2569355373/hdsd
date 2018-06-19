package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.main.MainActivity;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.sqllites.SharedPreferences_operate;


import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;


public class SetActivity extends BaseActivity implements OnClickListener{
	List<TextView> list = new ArrayList<TextView>();
	private SharedPreferences_operate operate;
	TextView textview,yjfk,banbenhao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView title=(TextView) findViewById(R.id.title);
		title.setText("设置");
		banbenhao= (TextView) findViewById(R.id.banbenhao);
		banbenhao.setText(getVersion());
		operate=new SharedPreferences_operate("login", this);
		textview=(TextView) findViewById(R.id.tc_login);
		yjfk=(TextView)findViewById(R.id.yjfk);
		yjfk.setOnClickListener(this);
		textview.setOnClickListener(view -> {
            Intent intent=new Intent(SetActivity.this, LoginActivity.class);
            startActivity(intent);
            operate.addint("islogin", 0);
            operate.addString("token","");
            HdsdApplication.login=false;
            HdsdApplication.token="";
            MainActivity.Refresh=true;
            HdsdApplication.nickname="";
            HdsdApplication.hasUnRead="0";
            HdsdApplication.avatar="";
            HdsdApplication.id="";
            User.list_courseId.clear();
            HdsdApplication.getInstance().userType=false;
            finish();

        });

		if (HdsdApplication.login)
		{
			textview.setVisibility(View.VISIBLE);
			yjfk.setVisibility(View.VISIBLE);
		}
		else
		{
			textview.setVisibility(View.GONE);
			yjfk.setVisibility(View.GONE);
		}
		ImageButton back=(ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		list.add((TextView)findViewById(R.id.bzsm));
//		list.add((TextView)findViewById(R.id.yjfk));
		//list.add((TextView)findViewById(R.id.bbxx));
		list.add((TextView)findViewById(R.id.gywm));
//		list.add((TextView)findViewById(R.id.tc_login));
		settype(list);
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.set_layout;
	}

	void settype( List<TextView> list){
		if(list.size()!=0)
		{
			for(int i=0;i<list.size();i++)
			{

				list.get(i).setOnClickListener(this);
			}
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bzsm://帮助说明
			Intent intent1=new Intent(SetActivity.this, WebviewActivity.class);
			intent1.putExtra("title", "帮助说明");
			intent1.putExtra("url", User.url+"/index.php?mod=mobile&name=shopwap&do=sys&op=help");
			startActivity(intent1);
			break;
		case R.id.yjfk://意见反馈
			startActivity(new Intent(SetActivity.this, YiJianFanKuiActivity.class));
			break;
		case R.id.gywm://关于我们
			Intent intent2=new Intent(SetActivity.this, WebviewActivity.class);
			intent2.putExtra("title", "关于我们");
			intent2.putExtra("url", User.url+"/index.php?mod=mobile&name=shopwap&do=sys&op=aboutus");
			startActivity(intent2);
			break;


		default:
			break;
		}
	}
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return  "V"+version;
		} catch (Exception e) {
			e.printStackTrace();
			return "V1.0";
		}
	}
}
