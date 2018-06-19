package com.zchd.hdsd.simpleactivity;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zchd.hdsd.HdsdApplication;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;
import com.zchd.hdsd.business.main.MainActivity;
import com.zchd.hdsd.tool.UpdateVersionTool;
import com.zchd.library.adapter.ViewPagerAdapter;
import com.zchd.library.sqllites.SharedPreferences_operate;

import java.io.InputStream;
import java.util.ArrayList;

import base.BaseActivity;


public class StartActivity extends BaseActivity{
	ViewPager viewpager;
	private SharedPreferences_operate operate;
	private ViewPagerAdapter adapter;
	ArrayList<View> viewContainter = new ArrayList<View>();
	ImageView image;
	int width,height;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		viewpager=(ViewPager) findViewById(R.id.start_viewpager);
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		 width = wm.getDefaultDisplay().getWidth();
		 height = wm.getDefaultDisplay().getHeight();
		operate=new SharedPreferences_operate("login", StartActivity.this);
		try {
			operate.addint("appcode", UpdateVersionTool.getVersionCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		operate.addint("start", 1);
		View view1 = LayoutInflater.from(this).inflate(R.layout.start_layout1, null);
		View view2 = LayoutInflater.from(this).inflate(R.layout.start_layout2, null);
		View view3 = LayoutInflater.from(this).inflate(R.layout.start_layout3, null);
//		View view4 = LayoutInflater.from(this).inflate(R.layout.start_layout4, null);
		ImageView start1=(ImageView) view1.findViewById(R.id.start_layout1_image);
		ImageView start2=(ImageView) view2.findViewById(R.id.start_layout2_image);
		ImageView start3=(ImageView) view3.findViewById(R.id.start_layout3_image);
//		ImageView start4=(ImageView) view4.findViewById(R.id.start_layout4_image);
		Glide.with(StartActivity.this).load(R.drawable.start1).into(start1);
		Glide.with(StartActivity.this).load(R.drawable.start2).into(start2);
		Glide.with(StartActivity.this).load(R.drawable.start3).into(start3);
//		Glide.with(StartActivity.this).load(R.drawable.start4).into(start4);

		viewContainter.add(view1);
		viewContainter.add(view2);
		viewContainter.add(view3);
//		viewContainter.add(view4);
		adapter = new ViewPagerAdapter(viewContainter);  
		viewpager.setAdapter(adapter);
		image=(ImageView) view3.findViewById(R.id.start_layout3_image);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] locations = new  int[2] ;//解决popupwindow显示问题
				viewpager.getLocationOnScreen(locations);
				android.util.Log.e("tag",locations.toString());
				HdsdApplication.setAvailableHight(locations[1]+viewpager.getHeight());//获取不含底部操作栏的坐标值
				startActivity(new Intent(StartActivity.this, MainActivity.class));
				finish();
			}
		});

		viewpager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			}

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {  
                case 0: 
                	break;
				}
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
		return R.layout.start_layout;
	}
}
