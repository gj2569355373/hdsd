package com.zchd.hdsd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zchd.hdsd.tool.DisplayUtil;

public class BannerRectLayoutCamer extends RelativeLayout {
	public BannerRectLayoutCamer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BannerRectLayoutCamer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BannerRectLayoutCamer(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth()/10*8;
		int childHightSize = (int) (childWidthSize / 3*2);//定义高度
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHightSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}