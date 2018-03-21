package com.zchd.library.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zchd.library.R;
import com.zchd.library.util.SizeUtils;


/**
 * Created by GJ on 2016/11/11.
 * 标签Textview
 */
public class LabelTextView extends TextView {
    private int mBgColor = 0;//默认颜色
    private int mPressColor = 0;//按下的颜色
    private int px=0;
    private boolean isPress = false;
    private LabelTextADD labelTextADD;

    public void setLabelTextADD(LabelTextADD labelTextADD) {
        this.labelTextADD = labelTextADD;
    }

    public boolean isPress() {
        return isPress;
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        this.setClickable(true);
    }
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        mBgColor = ta.getColor(R.styleable.LabelTextView_normalColor, Color.BLUE);
        mPressColor = ta.getColor(R.styleable.LabelTextView_pressColor, Color.RED);
        px= SizeUtils.dp2px(context,ta.getColor(R.styleable.LabelTextView_angle, 0));
        ta.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(!isPress) {
            init();
        }
        super.onDraw(canvas);
    }
    private void init() {
        setBackgroundRounded(this.getMeasuredWidth(), this.getMeasuredHeight(), this,mBgColor);
    }
    public void setBackgroundRounded(int w, int h, View v, int color)
    {
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);//Canvas画布
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);//FILTER_BITMAP_FLAG使位图过滤的位掩码标志
        paint.setAntiAlias(true);//抗锯齿，图像边缘相对清晰一点,锯齿痕迹不那么明显
        paint.setColor(color);
        RectF rec = new RectF(0, 0, w, h);
        c.drawRoundRect(rec, px, px, paint);//第二个参数是x半径，第三个参数是y半径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            //Android系统大于等于API16，使用setBackground
            v.setBackground(new BitmapDrawable(getResources(), bmp));
        } else {
            //Android系统小于API16，使用setBackground
            v.setBackgroundDrawable(new BitmapDrawable(getResources(), bmp));
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!isPress) {
                    if (getText().toString().equals("新建标签"))
                    {//弹出输入框
                        if (labelTextADD==null)
                            break;
                        else
                        {
                            showdialog();
                        }

                        break;
                    }
                    isPress = true;
                    setBackgroundRounded(this.getMeasuredWidth(), this.getMeasuredHeight(), this, mPressColor);
                }
                else
                {
                    isPress=false;
                    setBackgroundRounded(this.getMeasuredWidth(), this.getMeasuredHeight(), this, mBgColor);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                isPress = false;
//                init();
                break;
        }
        return super.onTouchEvent(event);
    }
    public interface LabelTextADD{
        public Activity getactivity();
        public int ViewGroupID();
        public int layoutID();
        public int getok();
        public int getdimms();
        public int getContent();
        public void addaddlable(String name);
    }
    public void showdialog(){
        LayoutInflater inflater =labelTextADD.getactivity().getLayoutInflater();
        AlertDialog dialog = null;
        View layout = inflater.inflate( labelTextADD.layoutID(),(ViewGroup) findViewById(labelTextADD.ViewGroupID()));
        TextView textViewok= (TextView) layout.findViewById(labelTextADD.getok());
        TextView textViewdimms= (TextView) layout.findViewById(labelTextADD.getdimms());
        final TextView textViewcontent= (TextView) layout.findViewById(labelTextADD.getContent());
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(labelTextADD.getactivity()).setView(layout);
        dialogBuilder.setCancelable(true);
        dialog=dialogBuilder.create();
        final AlertDialog finalDialog = dialog;
        textViewdimms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        textViewok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                labelTextADD.addaddlable( textViewcontent.getText().toString());
                finalDialog.dismiss();
            }
        });
        dialog.show();
    }
}
