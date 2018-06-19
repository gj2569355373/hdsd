package com.zchd.hdsd.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BannerRectLayoutCamers extends RelativeLayout {
	public BannerRectLayoutCamers(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BannerRectLayoutCamers(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BannerRectLayoutCamers(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHightSize = (int) (childWidthSize / 3*4);//定义高度
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHightSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}