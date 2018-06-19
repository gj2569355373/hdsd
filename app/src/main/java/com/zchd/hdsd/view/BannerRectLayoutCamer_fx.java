package com.zchd.hdsd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BannerRectLayoutCamer_fx extends RelativeLayout {
	public BannerRectLayoutCamer_fx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BannerRectLayoutCamer_fx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BannerRectLayoutCamer_fx(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth()/10*7;
		int childHightSize =childWidthSize;//定义高度
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHightSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}