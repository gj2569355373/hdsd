package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.model.User;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import okhttp3.Call;

public class XxjlActivity extends BaseActivity implements OnClickListener{
	List<TextView> list = new ArrayList<TextView>();
	RelativeLayout xxjl_xxsc,xxjl_weishipin,xxjl_tjzy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView title=(TextView) findViewById(R.id.title);
		title.setText("学习记录");

		ImageButton back=(ImageButton) findViewById(R.id.back);
		back=(ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		list.add((TextView)findViewById(R.id.xxjl_time));
		list.add((TextView)findViewById(R.id.xxjl_shiping));
		list.add((TextView)findViewById(R.id.xxjl_zuoye));
		settype(list);
		xxjl_xxsc=(RelativeLayout) findViewById(R.id.xxjl_xxsc);
		xxjl_xxsc.setOnClickListener(this);
		xxjl_weishipin=(RelativeLayout) findViewById(R.id.xxjl_weishipin);
		xxjl_weishipin.setOnClickListener(this);
		xxjl_tjzy=(RelativeLayout) findViewById(R.id.xxjl_tjzy);
		xxjl_tjzy.setOnClickListener(this);
		http();
	}

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {

	}

	@Override
	protected int getLayoutResID() {
		return R.layout.xxjl_layout;
	}


	private void http() {
		// TODO Auto-generated method stub
		Map<String, String>map=new HashMap<String, String>();
		map.put("userId", HdsdApplication.id);
		map.put("token",HdsdApplication.token);

		icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=vedio&op=learnVedioRecord", new TextLinstener(XxjlActivity.this) {
			
			@Override
			public void onresponse(String text) {
				// TODO Auto-generated method stub
				try {
					JSONObject object=new JSONObject(text);
					if(object.getString("code").equals("1"))
					{
						JSONObject object_result=object.getJSONObject("result");
						list.get(0).setText(object_result.getString("time"));
						if (Long.parseLong(object_result.getString("time"))/1000>60) {
							if (Long.parseLong(object_result.getString("time"))/1000/60>60) {
								int x=(int) (Long.parseLong(object_result.getString("time"))/1000/60/60);
								int y=(int) (Long.parseLong(object_result.getString("time"))-(x*60*60*1000));
								list.get(0).setText(x+"时"+y+"分");
							}
							list.get(0).setText(Long.parseLong(object_result.getString("time"))/1000/60+"分");
						}
						else
							list.get(0).setText(Long.parseLong(object_result.getString("time"))/1000+"秒");
						list.get(1).setText(object_result.getString("vedioCount")+"个");
						list.get(2).setText(object_result.getString("homeworkCount")+"次");
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
		}, map, XxjlActivity.this, "获取中");
	}



	void settype( List<TextView> list){
		if(list.size()!=0)
		{
			for(int i=0;i<list.size();i++)
			{
			//	list.get(i).setTypeface(Uitl.tf);
				list.get(i).setOnClickListener(this);
			}
		}
	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.xxjl_tjzy://提交作业
			
			break;
		case R.id.xxjl_weishipin://微视频
					
			break;
		case R.id.xxjl_xxsc://学习时长
			
			break;

		default:
			break;
		}
	}
}
