package com.zchd.hdsd.simpleactivity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.zchd.hdsd.view.NoDoubleClickListener;
import com.zchd.library.network.linstener.TextLinstener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

import okhttp3.Call;

public class PinglunActivity extends BaseActivity {
	EditText ed_text;
	TextView fabupinglun;
	int xingji=5;
	String courseId="";
	@BindView(R.id.yjfk_textsize)
	TextView yjfkTextsize;
	List<ImageButton>list_imagebutton=new ArrayList<ImageButton>();
	@Override

	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putString("courseId", courseId);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		courseId=savedInstanceState.getString("courseId");
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView title=(TextView) findViewById(R.id.title);
		title.setText("发表评价");
		if (courseId.equals(""))
			courseId=getIntent().getStringExtra("courseId");

		yjfkTextsize.setText("0/200");
		ed_text=(EditText) findViewById(R.id.pinlun_EditText);
		ed_text.addTextChangedListener(new TextWatcher() {//内容变化监听
			/**
			 * 文本变化前
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			/**
			 * 文本变化中
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				yjfkTextsize.setText(s.length()+"/200");
			}
			/**
			 * 文本变化之后
			 */
			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		ImageButton back=(ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		fabupinglun=(TextView) findViewById(R.id.fabiaopinglun);
		
		list_imagebutton.add((ImageButton)findViewById(R.id.xingji_bt1));
		list_imagebutton.add((ImageButton)findViewById(R.id.xingji_bt2));
		list_imagebutton.add((ImageButton)findViewById(R.id.xingji_bt3));
		list_imagebutton.add((ImageButton)findViewById(R.id.xingji_bt4));
		list_imagebutton.add((ImageButton)findViewById(R.id.xingji_bt5));
		setlist(list_imagebutton);
		list_imagebutton.get(0).setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            onXingji1();
        });
		list_imagebutton.get(1).setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            onXingji2();
        });
		list_imagebutton.get(2).setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            onXingji3();
        });
		list_imagebutton.get(3).setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            onXingji4();
        });
		list_imagebutton.get(4).setOnClickListener(arg0 -> {
            // TODO Auto-generated method stub
            onXingji5();
        });
		fabupinglun.setOnClickListener(new NoDoubleClickListener() {

			@Override
			public void onNoDoubleClick(View arg0) {
				// TODO Auto-generated method stub
				if (ed_text.getText().toString().trim().length()==0) {
					Toast.makeText(PinglunActivity.this,"评论内容不可为空！",Toast.LENGTH_SHORT).show();
					return;
				}
				if (ed_text.getText().toString().trim().length()<5) {
					Toast.makeText(PinglunActivity.this,"评论内容不可少于5个字！",Toast.LENGTH_SHORT).show();
					return;
				}
				if (ed_text.getText().toString().trim().length()>200) {
					Toast.makeText(PinglunActivity.this,"评论内容不可大于200个字！",Toast.LENGTH_SHORT).show();
					return;
				}
				Map<String, String>map=new HashMap<String, String>();
				map.put("userId",HdsdApplication.id);
				map.put("token", HdsdApplication.token);
				map.put("courseId",courseId);
				map.put("comment",ed_text.getText().toString());
				map.put("point",xingji+"");
				icssOkhttp.HttppostString(User.url+"/index.php?mod=site&name=api&do=course&op=evaluate", new TextLinstener(PinglunActivity.this) {

					@Override
					public void onresponse(String text) {
						// TODO Auto-generated method stub
						try {
							JSONObject object=new JSONObject(text);
							if(object.getString("code").equals("1"))
							{
								JSONObject obj=object.getJSONObject("result");
								Intent intent=new Intent();
								intent.putExtra("xingji",xingji );
								intent.putExtra("context",ed_text.getText().toString() );
								intent.putExtra("time",obj.getString("trans_time") );
							//	intent.putExtra("xingji",xingji );
								setResult(RESULT_OK, intent);
								finish();
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
				}, map, PinglunActivity.this, "发表中");
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
	@OnClick(R.id.xingji1)
	public void onXingji1(){
		xingji=1;
		for (int i = 0; i < list_imagebutton.size(); i++) {
			if(i<1)
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			else
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_off);
		}
	}
	@OnClick(R.id.xingji2)
	public void onXingji2(){
		xingji=2;
		for (int i = 0; i < list_imagebutton.size(); i++) {
			if(i<2)
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			else
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_off);
		}
	}
	@OnClick(R.id.xingji3)
	public void onXingji3(){
		xingji=3;
		for (int i = 0; i < list_imagebutton.size(); i++) {
			if(i<3)
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			else
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_off);
		}
	}
	@OnClick(R.id.xingji4)
	public void onXingji4(){
		xingji=4;
		for (int i = 0; i < list_imagebutton.size(); i++) {
			if(i<4)
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			else
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_off);
		}
	}
	
	
	@OnClick(R.id.xingji5)
	public void onXingji5(){
		xingji=5;
		for (int i = 0; i < list_imagebutton.size(); i++) {
			if(i<5)
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			else
				list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_off);
		}
	}
	void setlist(List<ImageButton>list_imagebutton)
	{
		for (int i = 0; i < list_imagebutton.size(); i++) {
			list_imagebutton.get(i).setBackgroundResource(R.drawable.comment_star_on);
			
		}
	}
	
}
