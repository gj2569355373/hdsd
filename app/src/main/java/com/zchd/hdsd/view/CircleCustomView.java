package com.zchd.hdsd.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by boling on 16/7/22.
 */
public class CircleCustomView extends View {

    private Paint paint;
    private Context mContext;

    public CircleCustomView(Context context) {
        super(context);
    }

    public CircleCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.parseColor("#c8c8c8"));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    public CircleCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, 20, paint);
    }
}
