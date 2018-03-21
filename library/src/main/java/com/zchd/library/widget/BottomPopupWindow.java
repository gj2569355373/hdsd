package com.zchd.library.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zchd.library.R;
import com.zchd.library.entity.ContentOnclick;
import com.zchd.library.util.SizeUtils;

import java.util.List;



/**
 * Created by GJ on 2016/11/28.
 *
 *
 * H 为距离底部多少高度
 */
public class BottomPopupWindow {
    private PopupWindow mPopupWindow=null;
    private View view=null;
    int[] locations = new  int[2] ;//为解决魅族手机底部虚拟按键显示问题
    public int AvailableHight;

    public BottomPopupWindow(int availableHight) {
        AvailableHight = availableHight;
    }

    public void show(Activity activity, View views, List<ContentOnclick>list ){
//        views.getLocationOnScreen(locations);
        view =  LayoutInflater.from(activity).inflate(R.layout.bottompopupwindow, null, false);
        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.bottompopupwindow_linearlayout);
        TextView textdimms= (TextView) view.findViewById(R.id.bottompopupwindow_dimms);
        textdimms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mPopupWindow=null;
            }
        });
        for (int i=0;i<list.size();i++) {
            linearLayout.addView(getLinearView(linearLayout.getContext(),list.get(i)));
            if ((i+1)<list.size())
                linearLayout.addView(getViewview(linearLayout.getContext()));
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPopupWindow.dismiss();
                    mPopupWindow=null;
                    return true;
                }
                return false;
            }
        });
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        int location[] = getScreenWH(activity);
        mPopupWindow.showAsDropDown(views,0,-(location[1]+views.getHeight()));
    };
    public View getViewview(Context context){
        View lineview=new View(context);
        lineview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
        lineview.setBackgroundColor(Color.parseColor("#202020"));
        return lineview;
    }
    public View getLinearView(Context context, final ContentOnclick contentonclick) {
        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textview=new TextView(context);
        textview.setLayoutParams(lp);
        textview.setGravity(Gravity.CENTER);
        textview.setPadding(SizeUtils.dp2px(context,15),SizeUtils.dp2px(context,15),SizeUtils.dp2px(context,15),SizeUtils.dp2px(context,15));//单位px
        textview.setTextSize(15);//单位sp
        textview.setTextColor(Color.parseColor("#217e7e"));
        textview.setText(contentonclick.getContent());
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentonclick.getI().onClick(v);
                mPopupWindow.dismiss();
                mPopupWindow=null;
            }
        });
        textview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView vt = (TextView)v;
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        vt.setTextColor(Color.parseColor("#217e7e"));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        vt.setTextColor(Color.parseColor("#217e7e"));
                        break;

                }
                return false;
            }
        });
        return textview;
    }
    public int getVrtualBtnHeight(Context poCotext) {//得到坐标差值

//        int location[] = getScreenWH(poCotext);
        int virvalHeight =  AvailableHight-locations[1];
        return virvalHeight;
    }
    public int[] getScreenWH(Context poCotext) {
        WindowManager wm = (WindowManager) poCotext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return new int[] { width, height };
    }
}
