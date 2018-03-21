
package com.zchd.library.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zchd.library.R;


public class Title extends RelativeLayout {

    /**
     * 上下文对象
     */
    private Context mContext = null;


    /**
     * 标题
     */
    private TextView mTitleTv = null;


    public Title(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public Title(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }

    public Title(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.title_layout, this, true);
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mTitleTv = (TextView)findViewById(R.id.title_title_tv);

    }

    /**
     * 设置返回按钮的监听器
     * 
     * @param listener 监听器
     */
    public void setBackListener(OnClickListener listener) {

        setTitlePadding();
    }

    /**
     * 设置标题栏标题
     * 
     * @param text 标题
     */
    public void setTitle(CharSequence text) {
        mTitleTv.setText(text);
    }

    /**
     * 设置标题栏标题
     */
    public void setTitle(int resId) {
        mTitleTv.setText(resId);
    }

    /**
     * 设置确定按钮监听器
     * 
     * @param listener
     */
    public void setSureListener(String text, OnClickListener listener) {

        setTitlePadding();
    }

    /**
     * 设置边距。当有左边图标或者右边图标的时候需要设置
     */
    private void setTitlePadding() {
        mTitleTv.setPadding(0, 0, 0, dp2px(mContext, 8.5F));
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * 
     * @param dipValue
     * @return
     */
    private int dp2px(Context ctx, float dipValue) {
        return (int)(dipValue * ctx.getResources().getDisplayMetrics().density + 0.5F);
    }
}
