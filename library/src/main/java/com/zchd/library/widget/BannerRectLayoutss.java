package com.zchd.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BannerRectLayoutss extends RelativeLayout {
	public BannerRectLayoutss(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BannerRectLayoutss(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BannerRectLayoutss(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHightSize =(childWidthSize/5*3)*2/3;//高=宽  定义高度
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHightSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}