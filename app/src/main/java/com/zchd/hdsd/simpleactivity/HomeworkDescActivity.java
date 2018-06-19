package com.zchd.hdsd.simpleactivity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zchd.hdsd.R;
import com.zchd.hdsd.business.component.ActivityComponent;

import base.BaseActivity;
import butterknife.BindView;


public class HomeworkDescActivity extends BaseActivity {
	
	@BindView(R.id.homework_url)
	ImageView mImageViewHomeworkUrl;
	@BindView(R.id.homework_mark)
	TextView mTextViewHomeworkMark;
	@BindView(R.id.title)
	TextView mTextViewTitle;
	@BindView(R.id.gif1)
	ImageView gf;
	@BindView(R.id.is_show)
	RelativeLayout mRelativeLayoutShow;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	protected void setComponent(ActivityComponent activityComponent) {

	}

	@Override
	protected void setDataBinding(ViewDataBinding dataBinding) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Glide.with(this).load(getIntent().getStringExtra("homeWorkUrl")).into(mImageViewHomeworkUrl);
		mTextViewHomeworkMark.setText(getIntent().getStringExtra("homeWorkContent"));
		mTextViewTitle.setText("作业预览");
		int commentStatus = getIntent().getIntExtra("commentStatus", -1);
		if (commentStatus == 0) {
			mRelativeLayoutShow.setVisibility(View.VISIBLE);
			mTextViewHomeworkMark.setVisibility(View.GONE);
			GlideGif(R.drawable.homework_commenting,gf);
//			Glide.with(HomeworkDescActivity.this).load(R.drawable.homework_commenting).into(gf);
		} else {
			mRelativeLayoutShow.setVisibility(View.GONE);
			mTextViewHomeworkMark.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.back).setOnClickListener(onClickListener);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_homework_desc;
	}

}
